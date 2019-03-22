package ivg.cn.monitor.kafka.service;

import java.util.concurrent.ConcurrentHashMap;

import javax.management.remote.JMXConnector;

import ivg.cn.monitor.kafka.JmxUtil;
import ivg.cn.monitor.kafka.bo.NodeBO;

/**
 * 通过ClusterService获取到的Node，创建并管理JMX连接
 * */
public class JMXConnectorService {

	ConcurrentHashMap<Integer, JMXConnector> jmxConnectorMap = new ConcurrentHashMap<>();
	BrokerService brokerService;
	
	public JMXConnector getAndCreate(int id) {
		JMXConnector jmxConnector = jmxConnectorMap.get(id);
		if (jmxConnector != null) {
			return jmxConnector;
		}
		synchronized (this) {
			jmxConnector = jmxConnectorMap.get(id);
			if (jmxConnector != null) {
				return jmxConnector;
			}
			jmxConnector = createJmxConnector(id);
			if (jmxConnector != null) {
				jmxConnectorMap.putIfAbsent(id, jmxConnector);
			}
		}
		return jmxConnector;
	}
	
	public JMXConnector createJmxConnector(int id) {
		NodeBO nodeBO = this.brokerService.nodeBO(id);
		if (nodeBO != null && nodeBO.getJmxPort() >0) {
			return JmxUtil.createConnector(nodeBO.getHost()+":"+nodeBO.getJmxPort());
		}
		return null;
	}
	
	public void setBrokerService(BrokerService brokerService) {
		this.brokerService = brokerService;
	}

}
