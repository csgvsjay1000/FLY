package ivg.cn.kclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.admin.DescribeConsumerGroupsResult;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.junit.Before;
import org.junit.Test;

public class KClientAdminTest {

	@Before
	public void init() {
		
	}
	
	
	@Test
	public void testListTopic() throws InterruptedException, ExecutionException {
		
		Properties properties = new Properties();
		properties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.5.131:10200");
		
		
		AdminClient adminClient = AdminClient.create(properties);
		ListTopicsResult topicsResult = adminClient.listTopics();
		System.out.println(topicsResult.toString());
		
		DescribeClusterResult clusterResult = adminClient.describeCluster();
		System.out.println(clusterResult);
		
		adminClient.listConsumerGroups();
		
		ListConsumerGroupOffsetsResult result = adminClient.listConsumerGroupOffsets("test");
		Map<TopicPartition, OffsetAndMetadata> map = result.partitionsToOffsetAndMetadata().get();
		for(Map.Entry<TopicPartition, OffsetAndMetadata> offset :  map.entrySet()){
			TopicPartition topicPartition = offset.getKey();
			OffsetAndMetadata metadata = offset.getValue();
			
			System.out.println("topic:"+topicPartition.topic()+", offset:"+metadata.offset());
			
		}
		
		DescribeConsumerGroupsResult consumerGroupsResult = adminClient.describeConsumerGroups(Arrays.asList("test"));
		Map<String, ConsumerGroupDescription> map2 = consumerGroupsResult.all().get();
		
		for(Map.Entry<String, ConsumerGroupDescription> entry : map2.entrySet()){
			String key = entry.getKey();
			ConsumerGroupDescription value = entry.getValue();
			
		}
		
		adminClient.describeTopics(Arrays.asList("test"));
	}
	
	@Test
	public void testTopicMessage() {
		Properties props = new Properties();
        props.put("bootstrap.servers", "39.108.11.154:8078");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "false");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        
        // test-topic
        // printEpcTopic
        
        
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        List<PartitionInfo> partitionInfos = consumer.partitionsFor("printEpcTopic");
        if (partitionInfos == null) {
        	consumer.close();
			return;
		}
        List<TopicPartition> tps = new ArrayList<>();
        for (PartitionInfo p : partitionInfos) {
        	TopicPartition tp = new TopicPartition(p.topic(), p.partition());
        	tps.add(tp);
		}
        Map<TopicPartition, Long> beginOffsets = consumer.beginningOffsets(tps);
        Map<TopicPartition, Long> endOffsets = consumer.endOffsets(tps);
        
        long total = 0;
        for (TopicPartition tp : tps) {
			long offset = endOffsets.get(tp) - beginOffsets.get(tp);
			total += offset;
		}
        System.out.println("total messages: "+total);
        consumer.close();
	}
}
