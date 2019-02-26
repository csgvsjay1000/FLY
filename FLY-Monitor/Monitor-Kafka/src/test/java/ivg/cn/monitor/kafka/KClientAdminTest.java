package ivg.cn.monitor.kafka;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.admin.DescribeConsumerGroupsResult;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;
import org.apache.kafka.clients.admin.ListConsumerGroupsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.junit.Before;
import org.junit.Test;

public class KClientAdminTest {

	KafkaAdminClient kafkaAdminClient;
	
	@Before
	public void init() {
		Properties properties = new Properties();
		properties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.5.131:10201");
		
		
		kafkaAdminClient =  (KafkaAdminClient) AdminClient.create(properties);
	}
	
	
	@Test
	public void testListTopic() throws InterruptedException, ExecutionException {
		
		ListTopicsResult listTopicsResult = kafkaAdminClient.listTopics();;
		try {
			Set<String> topics = listTopicsResult.names().get();
			if (topics != null) {
				for (String string : topics) {
					System.out.println(string);
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCreateTopic() throws InterruptedException, ExecutionException {
		
		NewTopic topic = new NewTopic("inventory_topic", 3, (short)1);
		
		CreateTopicsResult result = kafkaAdminClient.createTopics(Arrays.asList(topic));
		
		for(Map.Entry<String, KafkaFuture<Void>> entry : result.values().entrySet()){
			Object object = entry.getValue().get();
			System.out.println(object);
		}
		
	}
	
	@Test
	public void testDeleteTopic() throws InterruptedException, ExecutionException {
		
		DeleteTopicsResult result = kafkaAdminClient.deleteTopics(Arrays.asList("printEpcTopic"));
		
		for(Map.Entry<String, KafkaFuture<Void>> entry : result.values().entrySet()){
			Object object = entry.getValue().get();
			System.out.println(object);
		}
		
	}
	
	@Test
	public void testTopicOffset() throws InterruptedException, ExecutionException {
		
		
		ListConsumerGroupOffsetsResult result = kafkaAdminClient.listConsumerGroupOffsets("test");
		Map<TopicPartition, OffsetAndMetadata> map = result.partitionsToOffsetAndMetadata().get();
		for(Map.Entry<TopicPartition, OffsetAndMetadata> offset :  map.entrySet()){
			TopicPartition topicPartition = offset.getKey();
			OffsetAndMetadata metadata = offset.getValue();
			
			System.out.println("topic:"+topicPartition.topic()+", partition:"+topicPartition.partition()+", offset:"+metadata.offset());
			
		}
		
		DescribeConsumerGroupsResult consumerGroupsResult = kafkaAdminClient.describeConsumerGroups(Arrays.asList("test"));
		Map<String, ConsumerGroupDescription> map2 = consumerGroupsResult.all().get();
		
		for(Map.Entry<String, ConsumerGroupDescription> entry : map2.entrySet()){
			String key = entry.getKey();
			ConsumerGroupDescription value = entry.getValue();
			
			System.out.println(value);
		}
		
	}
	
	
	@Test
	public void testListGroup() throws InterruptedException, ExecutionException {
		
		
		ListConsumerGroupsResult result = kafkaAdminClient.listConsumerGroups();
		Collection<ConsumerGroupListing> groupListings = result.valid().get();
		if (groupListings != null) {
			System.out.println(groupListings);
		}
		
		
		
	}
	
}
