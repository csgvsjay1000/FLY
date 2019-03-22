package ivg.cn.monitor.kafka;

import java.io.IOException;
import java.math.BigDecimal;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ivg.cn.monitor.kafka.vo.BytesInPerSec;
import ivg.cn.monitor.kafka.vo.UnderReplicatedPartitions;

public class JmxUtil {

	static Logger logger = LoggerFactory.getLogger(JmxUtil.class);
	
	static final String MeanRate = "MeanRate";
	static final String OneMinuteRate = "OneMinuteRate";
	static final String FifteenMinuteRate = "FifteenMinuteRate";
	
	static final String Value = "Value";
	
	/**
	 * 创建JMXConnector
	 * @param url    ip:jmxport
	 * */
	public static JMXConnector createConnector(String url) {
		String jmxUrl = "service:jmx:rmi:///jndi/rmi://" +url+ "/jmxrmi";
		
		try {
			JMXServiceURL serviceURL = new JMXServiceURL(jmxUrl);
			return JMXConnectorFactory.connect(serviceURL);
		} catch (IOException e) {
			logger.error("createConnector err.",e);
			return null;
		}
		
	}
	
	/**
	 * 消息入网速率（字节数）
	 * */
	public static BytesInPerSec byteInPerSec(MBeanServerConnection connection) {
		
		String objectNameStr = "kafka.server:type=BrokerTopicMetrics,name=BytesInPerSec";
		try {
			ObjectName objectName = new ObjectName(objectNameStr);
			AttributeList attributeList = connection.getAttributes(objectName, 
					new String[] {MeanRate,OneMinuteRate,FifteenMinuteRate});
			BytesInPerSec bSec = new BytesInPerSec();
			if (attributeList != null) {
				for (Object object : attributeList) {
					if (object instanceof Attribute) {
						Attribute map = (Attribute) object;
						Double value = (Double) map.getValue();
						BigDecimal bigDecimal = BigDecimal.valueOf(value);
						bigDecimal = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_EVEN);
						String name = map.getName();
						if (MeanRate.equals(name)) {
							bSec.setMeanRate(bigDecimal);
						}else if (OneMinuteRate.equals(name)) {
							bSec.setOneMinuteRate(bigDecimal);
						}else if (FifteenMinuteRate.equals(name)) {
							bSec.setFifteenMinuteRate(bigDecimal);
						}
					}
				}
				return bSec;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 消息入网速率（字节数）
	 * */
	public static UnderReplicatedPartitions underReplicatedPartitions(MBeanServerConnection connection) {
		
		String objectNameStr = "kafka.server:type=ReplicaManager,name=UnderReplicatedPartitions";
		try {
			ObjectName objectName = new ObjectName(objectNameStr);
			AttributeList attributeList = connection.getAttributes(objectName, 
					new String[] {Value});
			UnderReplicatedPartitions bSec = new UnderReplicatedPartitions();
			if (attributeList != null) {
				for (Object object : attributeList) {
					if (object instanceof Attribute) {
						Attribute map = (Attribute) object;
						Integer value = (Integer) map.getValue();
						String name = map.getName();
						if (Value.equals(name)) {
							bSec.setUnderReplicatedPartitions(value);
						}
					}
				}
				return bSec;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	kafka.log:type=Log,name=LogEndOffset,topic=test_topic_274,partition=0
	
	public static long logEndOffset(MBeanServerConnection connection,String topic,int part) {
		String objectNameStr = String.format("kafka.log:type=Log,name=LogEndOffset,topic=%s,partition=%d",topic,part);
		try {
			ObjectName objectName = new ObjectName(objectNameStr);
			AttributeList attributeList = connection.getAttributes(objectName, 
					new String[] {Value});
			if (attributeList != null) {
				for (Object object : attributeList) {
					if (object instanceof Attribute) {
						Attribute map = (Attribute) object;
						Long value = (Long) map.getValue();
						String name = map.getName();
						if (Value.equals(name)) {
							return value;
						}
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
}
