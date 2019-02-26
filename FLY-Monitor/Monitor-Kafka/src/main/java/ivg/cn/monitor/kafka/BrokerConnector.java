package ivg.cn.monitor.kafka;

import javax.management.remote.JMXConnector;

import org.apache.kafka.clients.admin.AdminClient;

public class BrokerConnector {
	private final JMXConnector jmxConnector;
	
	private final AdminClient adminClient;

	public BrokerConnector(JMXConnector jmxConnector, AdminClient adminClient) {
		super();
		this.jmxConnector = jmxConnector;
		this.adminClient = adminClient;
	}
	
	public JMXConnector getJmxConnector() {
		return jmxConnector;
	}
	
	public AdminClient getAdminClient() {
		return adminClient;
	}
}
