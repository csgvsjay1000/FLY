package ivg.cn.monitor.kafka;

import java.util.Properties;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;


public class ACUtils {

	public static AdminClient getAdminClient(String serverAddr) {
		Properties properties = new Properties();
		properties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddr);
		return AdminClient.create(properties);
	}
	
}
