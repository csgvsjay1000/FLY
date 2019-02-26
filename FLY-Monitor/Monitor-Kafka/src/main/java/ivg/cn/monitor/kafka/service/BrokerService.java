package ivg.cn.monitor.kafka.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.KafkaFuture;

import ivg.cn.common.ExecutorUtils;
import ivg.cn.monitor.kafka.BrokerConnector;
import ivg.cn.monitor.kafka.MonitorKafkaController;

public class BrokerService {

	ScheduledExecutorService topicScheduled;
	final MonitorKafkaController kafkaController;
	
	ConcurrentHashMap<String, BrokerConnector> topicMap = new ConcurrentHashMap<>();
	

	
	public BrokerService(MonitorKafkaController kafkaController) {
		this.kafkaController = kafkaController;
	}
	
	public void checkAndPut(String topic, BrokerConnector brokerConnector) {
		if (!topicMap.containsKey(topic)) {
			topicMap.put(topic, brokerConnector);
		}
	}
	
	public void start() {
		topicScheduled = ExecutorUtils.namedSingleScheduleExecutor("topicScheduled");
		topicScheduled.scheduleAtFixedRate(topicDescTask(), 2000,10000, TimeUnit.MILLISECONDS);
	}
	
	public Runnable topicDescTask() {
		return new Runnable() {
			
			@Override
			public void run() {
				
				for(Entry<String, BrokerConnector> entry : topicMap.entrySet()){
					kafkaController.submitTask(topicDescImpl(entry.getKey(), entry.getValue()));
				}
			}
		};
	}
	
	private Runnable topicDescImpl(String topic, BrokerConnector brokerConnector) {
		
		return new Runnable() {
			
			@Override
			public void run() {
				AdminClient adminClient = brokerConnector.getAdminClient();
				DescribeTopicsResult result = adminClient.describeTopics(Arrays.asList(topic));
				Map<String, KafkaFuture<TopicDescription>> topicMap = result.values();
				if (topicMap != null) {
					for(KafkaFuture<TopicDescription> kafkaFuture : topicMap.values()){
						try {
							TopicDescription topicDescription = kafkaFuture.get();
							System.out.println(topicDescription);
						} catch (InterruptedException | ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		};
		
	}
	
}
