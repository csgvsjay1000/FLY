package ivg.cn.monitor.kafka.service;

import ivg.cn.monitor.kafka.bo.NodeBO;
import ivg.cn.monitor.kafka.listener.BrokerListener;
import ivg.cn.monitor.kafka.watcher.BrokerIdsWatcher;

public class BrokerService implements BrokerListener{

	BrokerIdsWatcher brokerIdsWatcher;
	
	public boolean isAlive(int brokerId) {
		return brokerIdsWatcher.isAlive(brokerId);
	}
	
	public NodeBO nodeBO(int brokerId) {
		return brokerIdsWatcher.nodeBO(brokerId);
	}
	
	public void setBrokerIdsWatcher(BrokerIdsWatcher brokerIdsWatcher) {
		this.brokerIdsWatcher = brokerIdsWatcher;
	}

	@Override
	public void online(NodeBO nodeBO) {
		System.out.println("online:"+nodeBO);

	}

	@Override
	public void offline(NodeBO nodeBO) {
		System.out.println("offline:"+nodeBO);
	}
	
	
}
