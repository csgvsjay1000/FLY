package ivg.cn.monitor.comp.zk;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ZKReactor {

	Selector tSelector;
	BlockingQueue<ZKConnection> zkConnQueue = new LinkedBlockingQueue<>();

	public ZKReactor() {
		try {
			tSelector = Selector.open();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void start() {
		new Thread("zk-selector"){
			@Override
			public void run() {
				
				while (true) {
					try {
						
						tSelector.select(500);
						regConn();
						Set<SelectionKey> keys = tSelector.selectedKeys();
						try {
							for (SelectionKey key : keys) {
								if (key.isValid() && key.isReadable()) {
									ZKConnection conn = (ZKConnection) key.attachment();
									if (conn != null) {
										conn.read();
									}
								}
							}
						} finally {
							keys.clear();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
		}.start();
	}
	
	public void postConn(ZKConnection conn) {
		zkConnQueue.offer(conn);
		tSelector.wakeup();
	}
	
	private void regConn() {
		ZKConnection conn = null;
		while ((conn = zkConnQueue.poll()) != null) {
			if (conn.getChannel() != null) {
				try {
					conn.getChannel().register(tSelector, SelectionKey.OP_READ, conn);
				} catch (ClosedChannelException e) {
					e.printStackTrace();
					conn.close();
				}
			}
		}
	}
	
}
