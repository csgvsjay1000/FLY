package ivg.cn.monitor.kafka;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class ZKUtils {

	public static ZooKeeper createConnection(String connectString,int sessionTimeout) {
		try {
			CountDownLatch latch = new CountDownLatch(1);
			ZooKeeper zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
				
				@Override
				public void process(WatchedEvent event) {
					if ( KeeperState.SyncConnected == event.getState()) {
						latch.countDown();
					}
				}
			});
			latch.await(3,TimeUnit.SECONDS);
			return zk;
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
