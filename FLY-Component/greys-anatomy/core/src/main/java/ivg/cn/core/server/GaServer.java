package ivg.cn.core.server;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

import ivg.cn.core.Configure;
import ivg.cn.core.util.GaCheckUtils;
import ivg.cn.core.util.LogUtil;

class GaAttachment{
	private final int bufferSize;
    private final Session session;
    
    private LineDecodeState lineDecodeState;
    private ByteBuffer lineByteBuffer;
    
    public GaAttachment(int bufferSize, Session session) {
		this.bufferSize = bufferSize;
		this.session = session;
		this.lineByteBuffer = ByteBuffer.allocate(bufferSize);
		this.lineDecodeState = LineDecodeState.READ_CHAR;
	}
    
    /**  存储客户端输入命令 */
    public void put(byte data) {
		if (lineByteBuffer.hasRemaining()) {
			lineByteBuffer.put(data);
		}else {
			// 内存不够，重新分配一个更大的内存，
			// 然后把原数据装入, 
			ByteBuffer newBuffer = ByteBuffer.allocate(lineByteBuffer.capacity()+bufferSize);
			lineByteBuffer.flip();  // 由可写变成可读状态
			newBuffer.put(lineByteBuffer);
			newBuffer.put(data);
			this.lineByteBuffer = newBuffer;
		}
	}
    
    public String clearAndGetLine(Charset charset) {
		// 清空输入缓冲区，并返回输入命令
    	// 由写状态变成读状态
    	lineByteBuffer.flip();
    	int len = lineByteBuffer.limit();  // 获取可读数据长度
    	byte[] data = new byte[len];
    	lineByteBuffer.get(data);
    	lineByteBuffer.clear(); // 变成可写状态
    	return new String(data, charset);
	}
    
    public Session getSession() {
		return session;
	}
    
    public LineDecodeState getLineDecodeState() {
		return lineDecodeState;
	}
    
    public void setLineDecodeState(LineDecodeState lineDecodeState) {
		this.lineDecodeState = lineDecodeState;
	}
}

/**
 * 行解码
 */
enum LineDecodeState {

    // 读字符
    READ_CHAR,

    // 读换行
    READ_EOL
}

public class GaServer {

	private final Logger logger = LogUtil.getLogger();
	
	private static final int BUFFER_SIZE = 4 * 1024;
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    
    private static final byte CTRL_D = 0x04;
    private static final byte CTRL_X = 0x18;
    private static final byte EOT = 0x04;
    private static final int EOF = -1;
    
    private final int javaPid;
    
    private SessionManager sessionManager;
    private CommandHandler commandHandler;
    
    private final AtomicBoolean isBindRef = new AtomicBoolean(false);
    private ServerSocketChannel serverSocketChannel = null;
    private Selector selector = null;
    
    private ExecutorService executorService = Executors.newCachedThreadPool(new ThreadFactory() {
		
		@Override
		public Thread newThread(Runnable r) {
			final Thread t = new Thread(r, "ga-command-execute-daemon");
			t.setDaemon(true);
			return t;
		}
	});
    
    public GaServer(int javaPid, Instrumentation inst) {
    	logger.info("GaServer.ClassLoader : "+this.getClass().getClassLoader());
    	this.javaPid = javaPid;
    	this.sessionManager = new DefaultSessionManager();
    	this.commandHandler = new DefaultCommandHandler(this, inst);
	}
    
    public void bind(Configure configure) throws IOException {
		if (!isBindRef.compareAndSet(false, true)) {
			throw new IllegalStateException("already bind");
		}
		try {
			serverSocketChannel = ServerSocketChannel.open();
			selector = Selector.open();
			
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().setSoTimeout(configure.getConnectTimeout());
			serverSocketChannel.socket().setReuseAddress(true);
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			
			// 服务器绑定端口
			serverSocketChannel.socket().bind(getInetSocketAddress(configure.getTargetIp(), configure.getTargetPort()),24);
			logger.info("ga-server listening on network={};port={};timeout={};", configure.getTargetIp(),
                    configure.getTargetPort(),
                    configure.getConnectTimeout());
			
			activeSelectorDaemon(selector, configure);
			
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
    
    private void activeSelectorDaemon(final Selector selector, final Configure configure) {
		final ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);  // 分配4K内存
		
		final Thread gaServerSelectorDaemon = new Thread("ga-selector-daemon"){
			@Override
			public void run() {
				while (!isInterrupted() && isBind()) {
					try {
						while (selector.isOpen() && selector.select()>0) {
							Iterator<SelectionKey> it = selector.selectedKeys().iterator();
							while (it.hasNext()) {
								SelectionKey key = it.next();
								it.remove();
								
								if (key.isValid() && key.isAcceptable()) {
									// do ssc
									acceptSocketChannel(selector, (ServerSocketChannel)key.channel(), configure);
								}
								
								if (key.isValid() && key.isReadable()) {
									doRead(byteBuffer, key);
								}
								
							}
						}
					} catch (IOException e) {
                        logger.warn("selector failed.", e);
					}
				}
			}
		};
		gaServerSelectorDaemon.setDaemon(true);
		gaServerSelectorDaemon.start();
	}
    
    private void acceptSocketChannel(Selector selector, ServerSocketChannel serverSocketChannel, Configure configure) throws IOException {
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);
		socketChannel.socket().setTcpNoDelay(true);
		socketChannel.socket().setSoTimeout(configure.getConnectTimeout());
		
		final Session session = sessionManager.newSession(javaPid, socketChannel, DEFAULT_CHARSET);
		socketChannel.register(selector, SelectionKey.OP_READ, new GaAttachment(BUFFER_SIZE, session));
		
        logger.info("accept new connection, client={}]", socketChannel);

	}
    
    private void doRead(final ByteBuffer byteBuffer, SelectionKey key) {
		
    	GaAttachment attachment = (GaAttachment) key.attachment();
    	SocketChannel socketChannel = (SocketChannel) key.channel();
    	final Session session = attachment.getSession();
    	
    	try {
			if (EOF == socketChannel.read(byteBuffer)) {
				// 若读取数量为-1表示对方关闭连接
				return;
			}
			
			byteBuffer.flip();  // 可写变可读
			while (byteBuffer.hasRemaining()) {
				switch (attachment.getLineDecodeState()) {
				case READ_CHAR:
					final byte data = byteBuffer.get();
					if ('\n' == data) {
						// 遇见换行符，即为读取结束
						attachment.setLineDecodeState(LineDecodeState.READ_EOL);  
					}else if (CTRL_D == data || CTRL_X == data) {
						// 如果遇见终止命令
						
					}else if('\r' != data) {
						attachment.put(data);
						break;
					}
					
				case READ_EOL:
					// 读取到结束符
					final String line = attachment.clearAndGetLine(DEFAULT_CHARSET);
					logger.info("receive a command [{}]",line);
					executorService.execute(new Runnable() {
						
						@Override
						public void run() {
							// 先获取会话锁
							if (session.tryLock()) {
								try {
									commandHandler.executeCommand(line, session);
								} catch (Exception e) {
									
								}finally {
									session.unlock();
								}
							}
						}
					});
					attachment.setLineDecodeState(LineDecodeState.READ_CHAR);
					break;
				default:
					break;
				}
			}
			byteBuffer.clear();
			
		} catch (IOException e) {
			logger.warn("GaServer.doRead err.",e);
			// 关闭
			IOUtils.closeQuietly(socketChannel);
			key.cancel();
		}
    	
	}
    
    /**
     * 判断服务端是否已经启动
     * */
    public boolean isBind() {
		return isBindRef.get();
	}
    
    
    public void destroy() {
		if (isBind()) {
			// 如果已经绑定端口，则解绑
			unbind();
		}
		// 关闭
		executorService.shutdown();
        logger.info("ga-server destroy completed.");
	}
    
    private void unbind() {
		IOUtils.closeQuietly(serverSocketChannel);
		IOUtils.closeQuietly(selector);
		if (!isBindRef.compareAndSet(true, false)) {
			// 之前已经解绑
			 throw new IllegalStateException("already unbind");
		}
	}
    
    /*
     * 获取绑定网络地址信息<br/>
     * 这里做个小修正,如果targetIp为127.0.0.1(本地环回口)，则需要绑定所有网卡
     * 否则外部无法访问，只能通过127.0.0.1来进行了
     */
    private InetSocketAddress getInetSocketAddress(String targetIp, int targetPort) {
        if (GaCheckUtils.isEquals("127.0.0.1", targetIp)) {
            return new InetSocketAddress(targetPort);
        } else {
            return new InetSocketAddress(targetIp, targetPort);
        }
    }
    
    private static volatile GaServer gaServer;

    /**
     * 单例
     *
     * @param instrumentation JVM增强
     * @return GaServer单例
     */
    public static GaServer getInstance(final int javaPid, final Instrumentation instrumentation) {
        if (null == gaServer) {
            synchronized (GaServer.class) {
                if (null == gaServer) {
                    gaServer = new GaServer(javaPid, instrumentation);
                }
            }
        }
        return gaServer;
    }
	
}
