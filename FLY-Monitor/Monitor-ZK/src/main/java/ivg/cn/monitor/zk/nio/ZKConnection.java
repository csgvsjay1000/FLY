package ivg.cn.monitor.zk.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ZKConnection {

	static long DefaultTimeout = 3000;
	
	ByteBuffer readBuffer;
	SocketChannel channel;
	ZKReactor reactor;
	ZKSendCmdResult result;
	String ip;
	int port;
	
	public ZKConnection(ZKReactor reactor, String ip, int port) {
		this.reactor = reactor;
		this.ip = ip;
		this.port = port;
	}
	
	public void openConn() throws IOException {
		readBuffer = ByteBuffer.allocate(4096);
		channel = SocketChannel.open(new InetSocketAddress(ip, port));
		channel.configureBlocking(false);
		reactor.postConn(this);
	}
	
	/**
	 * 发送四字命令
	 * <br>
	 * conf
	 * stat
	 * */
	public ZKSendCmdResult sendCmd(String cmd) {
		try {
			openConn();
			result = new ZKSendCmdResult(cmd);
			channel.write(ByteBuffer.wrap(cmd.getBytes()));
			result.waitResult(DefaultTimeout);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public void read() {
		try {
			readBuffer.clear();
			int got = channel.read(readBuffer);
			onRead(got);
		} catch (IOException e) {
			try {
				channel.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	private void onRead(int got) {
		System.out.println("got:"+got);
		if (got <= 0) {
			this.close();
		}
		int len = readBuffer.position();
		byte[] data = new byte[len];
		readBuffer.position(0);
		readBuffer.get(data);
		String ret = new String(data);
		result.putResult(ret);
	}
	
	public void close() {
		try {
			channel.close();
			channel = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public SocketChannel getChannel() {
		return channel;
	}
	
}
