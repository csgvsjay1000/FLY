package ivg.cn.monitor.kafka;

import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

import ivg.cn.monitor.kafka.bo.NodeBO;
import ivg.cn.monitor.kafka.listener.BrokerListener;
import ivg.cn.monitor.kafka.watcher.BrokerIdsWatcher;

public class BrokerIdTest {

	
	@Test
	public void testId() {
		
		ZooKeeper zooKeeper = ZKUtils.createConnection("192.168.5.131:10100", 5000);
		BrokerIdsWatcher brokerIdsWatcher = new BrokerIdsWatcher(zooKeeper, "/brokers/ids");
		brokerIdsWatcher.addListener(new BrokerListenerImpl());
		brokerIdsWatcher.watch();
		
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	class BrokerListenerImpl implements BrokerListener{

		@Override
		public void online(NodeBO nodeBO) {
			System.out.println("online:"+nodeBO);
		}

		@Override
		public void offline(NodeBO nodeBO) {
			System.out.println("offline:"+nodeBO);
			
		}
		
	}
	
}
