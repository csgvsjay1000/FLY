package ivg.cn.monitor.kafka;

import java.io.IOException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JmxUtil {

	static Logger logger = LoggerFactory.getLogger(JmxUtil.class);
	
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
	
	
}
