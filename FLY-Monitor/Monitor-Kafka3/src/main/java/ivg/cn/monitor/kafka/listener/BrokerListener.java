package ivg.cn.monitor.kafka.listener;

import ivg.cn.monitor.kafka.bo.NodeBO;

public interface BrokerListener extends ZKListener{

	void online(NodeBO nodeBO);
	
	void offline(NodeBO nodeBO);
	
}
