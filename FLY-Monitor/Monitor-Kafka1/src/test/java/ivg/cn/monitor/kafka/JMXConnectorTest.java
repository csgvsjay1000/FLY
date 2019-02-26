package ivg.cn.monitor.kafka;

import java.io.IOException;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;

import org.junit.Test;

import ivg.cn.monitor.kafka.bo.BrokerItemBean;
import ivg.cn.monitor.kafka.entity.BrokerItem;

public class JMXConnectorTest {

	@SuppressWarnings("unused")
	@Test
	public void testConnector() {
		
		JMXConnector connector = JmxUtil.createConnector("192.168.5.131:10210");
		while (true) {
			try {
				MBeanServerConnection connection = connector.getMBeanServerConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	@Test
	public void testBrokerBean() {
		
		BrokerItem brokerItem = new BrokerItem();
		brokerItem.setBrokerIp("192.168.5.131");
		brokerItem.setBrokerPort(10200);
		brokerItem.setJmxPort(10210);
		
		BrokerItemBean brokerItemBean = BrokerItemBean.create(brokerItem);
		BrokerItemBean bean2 = BrokerItemBean.create(brokerItem);
		
		System.out.println(bean2.equals(brokerItemBean));
		
	}
	
	
}
