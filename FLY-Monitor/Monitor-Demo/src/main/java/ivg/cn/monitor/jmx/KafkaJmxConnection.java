package ivg.cn.monitor.jmx;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class KafkaJmxConnection {

	private MBeanServerConnection conn;
	JMXConnector connector;
	
	String ipAndPort;
	
	public KafkaJmxConnection(String ipAndPort) {
		this.ipAndPort = ipAndPort;
	}
	
	public boolean init() {
		String jmxUrl = "service:jmx:rmi:///jndi/rmi://" +ipAndPort+ "/jmxrmi";
		
		try {
			JMXServiceURL jmxServiceURL = new JMXServiceURL(jmxUrl);
			connector = JMXConnectorFactory.connect(jmxServiceURL, null);
			conn = connector.getMBeanServerConnection();
			if (conn == null) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public void close() {
		if (connector != null) {
			try {
				connector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Map<String, Object> getValue(String topicName, String metric, Collection<String> attrs) {
		ObjectName objectName = null;
		try {
			objectName = new ObjectName(this.getObjectNameStr(topicName, metric));
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
			return null;
		}
		
		Map<String, Object> result = new HashMap<>(); 
		for (String string : attrs) {
			result.put(string, this.getAttribute(objectName, string));
		}
		return result;
	}
	
	private Object getAttribute(ObjectName objectName, String attr) {
		try {
			return conn.getAttribute(objectName, attr);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private String getObjectNameStr(String topicName, String metric) {
		if (topicName == null) {
			return metric;
		}
		return metric + ",topic=" +topicName;
	}
	
}
