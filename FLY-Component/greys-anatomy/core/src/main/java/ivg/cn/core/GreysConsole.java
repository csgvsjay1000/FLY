package ivg.cn.core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.commons.lang3.StringUtils;

import jline.console.ConsoleReader;
import jline.console.KeyMap;

/**  Greys 客户端 */
public class GreysConsole {
	private static final byte EOT = 0x04;
    private static final byte EOF = -1;
	
	private ConsoleReader console;
    private final Writer out;

	private Socket socket;
	private BufferedReader socketReader;
	private BufferedWriter socketWriter;
	
	private volatile boolean isRunning;
	
	public GreysConsole(InetSocketAddress address) throws IOException {
		// 1、初始化ConsoleReader
		// 2、创建socket连接
		// 3、激活读线程
		
		this.console = initConsoleReader();
		this.out = console.getOutput();
		this.socket = connect(address);
		this.socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		
		this.isRunning = true;
		this.activeConsoleReader();
		loopForWriter();
	}
	
	private ConsoleReader initConsoleReader() throws IOException {
		ConsoleReader console = new ConsoleReader(System.in, System.out);  // 绑定reader到标准输入输出流
		console.getKeys().bind(""+KeyMap.CTRL_D, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
		return console;
	}
	
	private Socket connect(InetSocketAddress address) throws IOException {
		Socket s = new Socket();
		s.connect(address);
		
		return s;
	}
	
	/**  激活读取console命令行线程 */
	private void activeConsoleReader() {
		final Thread socketThread = new Thread("ga-console-reader"){
			
			private StringBuilder lineBuffer = new StringBuilder();
			
			@Override
			public void run() {
				try {
					while (isRunning) {
						String line = console.readLine();
						
						// 如果是\结尾，则说明还有下文，需要对换行做特殊处理
                        if (StringUtils.endsWith(line, "\\")) {
                            // 去掉结尾的\
                            lineBuffer.append(line.substring(0, line.length() - 1));
                            continue;
                        } else {
                            lineBuffer.append(line);
                        }

                        final String lineForWrite = lineBuffer.toString();
                        lineBuffer = new StringBuilder();
                        
                        console.setPrompt(StringUtils.EMPTY);
                        
                        if (StringUtils.isNotBlank(lineForWrite)) {
                            socketWriter.write(lineForWrite + "\n");
                        } else {
                            socketWriter.write("\n");
                        }
                        socketWriter.flush();
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
			}
		};
		socketThread.setDaemon(true);
		socketThread.start();
	}
	
	/**  循环读取greys server的结果 */
	private void loopForWriter() {
		try {
			while (isRunning) {
				final int c = socketReader.read();
                if (c == EOF) {
                    break;
                }
                if (c == EOT) {
                    console.redrawLine();
                } else {
                    out.write(c);
                }
                out.flush();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void main(String... args) throws IOException {
        new GreysConsole(new InetSocketAddress(args[0], Integer.valueOf(args[1])));
    }
	
}
