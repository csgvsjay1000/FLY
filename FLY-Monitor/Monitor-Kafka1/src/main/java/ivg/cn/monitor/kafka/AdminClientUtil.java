package ivg.cn.monitor.kafka;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.admin.DescribeConsumerGroupsResult;
import org.apache.kafka.clients.admin.ListConsumerGroupsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;

import ivg.cn.monitor.kafka.bo.BrokerConnector;

public class AdminClientUtil {

	public static AdminClient createAdminClient(String url) {
		Properties properties = new Properties();
		properties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, url);
		AdminClient adminClient = AdminClient.create(properties);
		return adminClient;
	}
	
	public static Set<String> listTopic(BrokerConnector brokerConnector) throws InterruptedException, ExecutionException {
		ListTopicsResult listTopicsResult = brokerConnector.getAdminClient().listTopics();
		return listTopicsResult.names().get();
	}
	
	public static Collection<ConsumerGroupListing> listConsumerGroups(BrokerConnector brokerConnector) throws InterruptedException, ExecutionException {
		ListConsumerGroupsResult listTopicsResult = brokerConnector.getAdminClient().listConsumerGroups();
		return listTopicsResult.valid().get();
	}
	
	public static Map<String, ConsumerGroupDescription> describeConsumerGroups(BrokerConnector brokerConnector,Collection<String> groupIds) throws InterruptedException, ExecutionException {
		DescribeConsumerGroupsResult consumerGroupsResult = brokerConnector.getAdminClient().describeConsumerGroups(groupIds);

		Map<String, ConsumerGroupDescription> map2 = consumerGroupsResult.all().get();
		
		return map2;
	}
	
}
