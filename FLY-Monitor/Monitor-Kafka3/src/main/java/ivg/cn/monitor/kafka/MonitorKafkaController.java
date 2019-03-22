package ivg.cn.monitor.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.zookeeper.ZooKeeper;

import ivg.cn.monitor.kafka.service.BrokerService;
import ivg.cn.monitor.kafka.service.ConsumerGroupService;
import ivg.cn.monitor.kafka.service.JMXConnectorService;
import ivg.cn.monitor.kafka.service.TopicService;
import ivg.cn.monitor.kafka.watcher.BrokerIdsWatcher;
import ivg.cn.monitor.kafka.watcher.TopicCreateWatcher;
import ivg.cn.monitor.kafka.watcher.TopicDeleteWatcher;

public class MonitorKafkaController {

	BrokerService brokerService;
	TopicService topicService;
	JMXConnectorService jmxConnectorService;
	ConsumerGroupService consumerGroupService;
	
	public MonitorKafkaController() {

		AdminClient adminClient = ACUtils.getAdminClient("192.168.5.131:10200,192.168.5.131:10201,192.168.5.131:10202");
		
		brokerService = new BrokerService();
		topicService = new TopicService();
		jmxConnectorService = new JMXConnectorService();
		consumerGroupService = new ConsumerGroupService(adminClient);

		topicService.setBrokerService(brokerService);
		
		jmxConnectorService.setBrokerService(brokerService);
		
		consumerGroupService.setBrokerService(brokerService);
		consumerGroupService.setJmxConnectorService(jmxConnectorService);
		consumerGroupService.setTopicService(topicService);
	}
	
	public void start() {
		
		ZooKeeper zooKeeper = ZKUtils.createConnection("192.168.5.131:10100", 5000);
		
		BrokerIdsWatcher idsWatcher = new BrokerIdsWatcher(zooKeeper, "/brokers/ids");
		idsWatcher.addListener(brokerService);
		
		TopicCreateWatcher createWatcher = new TopicCreateWatcher(zooKeeper, "/brokers/topics");
		createWatcher.addListener(topicService);
		
		TopicDeleteWatcher deleteWatcher = new TopicDeleteWatcher(zooKeeper, "/admin/delete_topics");
		deleteWatcher.addListener(topicService);
		
		createWatcher.setTopicDeleteWatcher(deleteWatcher);
		
		brokerService.setBrokerIdsWatcher(idsWatcher);
		
		idsWatcher.watch();
		createWatcher.watch();
		deleteWatcher.watch();
		
		
	}
	
}
