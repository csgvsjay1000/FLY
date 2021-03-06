package ivg.cn.monitor.comp.zk;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ZKConnection {

	ByteBuffer readBuffer;
	SocketChannel channel;
	ZKReactor reactor;
	ZKSendCmdResult result;
	
	public ZKConnection(ZKReactor reactor) {
		this.reactor = reactor;
	}
	
	public void openConn(String url) throws IOException {
		readBuffer = ByteBuffer.allocate(4096);
		String[] str = url.split(":");
		channel = SocketChannel.open(new InetSocketAddress(str[0], Integer.parseInt(str[1])));
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
			openConn("39.108.11.154:2181");
			result = new ZKSendCmdResult(cmd);
			channel.write(ByteBuffer.wrap(cmd.getBytes()));
			result.waitResult();
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
