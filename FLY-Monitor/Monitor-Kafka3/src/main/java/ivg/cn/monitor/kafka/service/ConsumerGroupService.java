package ivg.cn.monitor.kafka.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.management.remote.JMXConnector;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.admin.DescribeConsumerGroupsOptions;
import org.apache.kafka.clients.admin.DescribeConsumerGroupsResult;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsOptions;
import org.apache.kafka.clients.admin.ListConsumerGroupOffsetsResult;
import org.apache.kafka.clients.admin.ListConsumerGroupsOptions;
import org.apache.kafka.clients.admin.ListConsumerGroupsResult;
import org.apache.kafka.clients.admin.MemberDescription;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ivg.cn.common.ExecutorUtils;
import ivg.cn.monitor.kafka.JmxUtil;
import ivg.cn.monitor.kafka.bo.ConsumerOffsetBO;
import ivg.cn.monitor.kafka.bo.TopicPartBO;

public class ConsumerGroupService {
	Logger log = LoggerFactory.getLogger(getClass());

	protected ScheduledExecutorService scheduledExecutorService;
	protected ThreadPoolExecutor poolExecutor;
	ConcurrentHashMap<TopicPartBO /***/, ConsumerOffsetBO> topicPartConsumerMap = new ConcurrentHashMap<>();
	
	TopicService topicService;
	BrokerService brokerService;
	JMXConnectorService jmxConnectorService;

	AdminClient adminClient;
	
	public ConsumerGroupService(AdminClient adminClient) {
		this.adminClient = adminClient;
		
		scheduledExecutorService = ExecutorUtils.namedSingleScheduleExecutor("ConsumerGroupScheduler");
		poolExecutor = ExecutorUtils.traceFixedThreadPool("ConsumerGroupPool", 5);
		
		scheduledExecutorService.scheduleAtFixedRate(listConsumerGroupsRunnable(), 5, 5, TimeUnit.SECONDS);
		scheduledExecutorService.scheduleAtFixedRate(logEndOffsetRunner(), 5, 5, TimeUnit.SECONDS);
		scheduledExecutorService.scheduleAtFixedRate(iterTopicPartConsumerMapRunner(), 5, 5, TimeUnit.SECONDS);
		
	}
	
	private Runnable listConsumerGroupsRunnable() {
		return new Runnable() {
			
			@Override
			public void run() {
				poolExecutor.submit(new Runnable() {
					
					@Override
					public void run() {
						listConsumerGroups();
					}
				});
			}
		};
	}
	
	private void listConsumerGroups() {
		ListConsumerGroupsOptions options = new ListConsumerGroupsOptions();
		options.timeoutMs(3000);
		ListConsumerGroupsResult result = adminClient.listConsumerGroups(options);
		try {
			Collection<ConsumerGroupListing> listings = result.all().get();
			if (listings != null && listings.size()>0) {
				List<String> gList = new ArrayList<>(listings.size());
				for(ConsumerGroupListing listing : listings){
					gList.add(listing.groupId());
				}
				describeConsumerGroups(gList);
			}
			
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	private void describeConsumerGroups(Collection<String> groupIds) {
		DescribeConsumerGroupsOptions options = new DescribeConsumerGroupsOptions();
		options.timeoutMs(3000);
		DescribeConsumerGroupsResult result = adminClient.describeConsumerGroups(groupIds, options);
		try {
			Map<String, ConsumerGroupDescription> map = result.all().get();
			if (map != null && map.size()>0) {
				for(Entry<String, ConsumerGroupDescription> entry : map.entrySet()){
					ConsumerGroupDescription description = entry.getValue();
					processConsumerGroup(description);
				}
				
				for(Entry<String, ConsumerGroupDescription> entry : map.entrySet()){
					listConsumerGroupOffsets(entry.getKey());
				}
			}
			
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	private void processConsumerGroup(ConsumerGroupDescription groupDescription) {
		Collection<MemberDescription> memberDescriptions = groupDescription.members();
		for (MemberDescription memberDescription : memberDescriptions) {
			if (memberDescription.assignment() != null && memberDescription.assignment().topicPartitions() != null) {
				for(TopicPartition topicPartition : memberDescription.assignment().topicPartitions()){
					TopicPartBO topicPartBO = new TopicPartBO();
					topicPartBO.setTopic(topicPartition.topic());
					topicPartBO.setPartition(topicPartition.partition());
					ConsumerOffsetBO offsetBO = topicPartConsumerMap.get(topicPartBO);
					
					// 判断map中是否有该分区的记录
					// 2、若有，则判断clientId是否为NOCONSUMER，若是则更新时间，若不是也更新
					// 3、若无，则放进map中
					// 3、遍历map，若值不是CONSUMER切过期的，则将值设为NOCONSUMER
					// 4、若key过期了则删除
					
					boolean toNewObj = true;
					if (offsetBO != null) {
						// 如果存在map中，则判断是否有改变
						if (memberDescription.clientId().equals(offsetBO.getClientId())) {
							// 时间更新
							toNewObj = false;
						}
						
					}
					if (toNewObj) {
						offsetBO = new ConsumerOffsetBO();
						offsetBO.setClientId(memberDescription.clientId());
						offsetBO.setTopicPartBO(topicPartBO);
						topicPartConsumerMap.put(topicPartBO, offsetBO);
					}
					offsetBO.getTopicPartBO().update();
					offsetBO.update();
				}
			}
		}
	}
	
	private void listConsumerGroupOffsets(String groupId) {
		ListConsumerGroupOffsetsOptions options = new ListConsumerGroupOffsetsOptions();
		options.timeoutMs(3000);
		ListConsumerGroupOffsetsResult result = adminClient.listConsumerGroupOffsets(groupId, options);
		try {
			Map<TopicPartition, OffsetAndMetadata> map = result.partitionsToOffsetAndMetadata().get();
			if (map != null && map.size()>0) {
				for(Entry<TopicPartition, OffsetAndMetadata> entry : map.entrySet()){
					TopicPartition topicPartition = entry.getKey();
					TopicPartBO topicPartBO = TopicPartBO.create(topicPartition);
					final ConsumerOffsetBO offsetBO = topicPartConsumerMap.get(topicPartBO);
					if (offsetBO != null) {
						offsetBO.setOffset(entry.getValue().offset());
					}
				}
			}
			
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	private Runnable logEndOffsetRunner() {
		return new Runnable() {
			
			@Override
			public void run() {
				poolExecutor.submit(new Runnable() {
					
					@Override
					public void run() {
						logEndOffset();
					}
				});
			}
		};
	}
	
	/**  
	 * 获取每个log的size
	 * */
	private void logEndOffset() {
		for(Entry<TopicPartBO /***/, ConsumerOffsetBO> entry : topicPartConsumerMap.entrySet()){
			TopicPartBO partBO = entry.getKey();
			ConsumerOffsetBO offsetBO = entry.getValue();
			Integer leader = topicService.leader(partBO);
			if (leader != null) {
				JMXConnector jmxConnector = jmxConnectorService.getAndCreate(leader);
				if (jmxConnector != null) {
					try {
						long logEndOffset = JmxUtil.logEndOffset(jmxConnector.getMBeanServerConnection(), partBO.getTopic(), partBO.getPartition());
						if (logEndOffset != offsetBO.getSize()) {
							offsetBO.setSize(logEndOffset);
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
		}
	}
	
	private Runnable iterTopicPartConsumerMapRunner() {
		return new Runnable() {
			
			@Override
			public void run() {
				poolExecutor.submit(new Runnable() {
					
					@Override
					public void run() {
						iterTopicPartConsumerMap();
					}
				});
			}
		};
	}
	
	private void iterTopicPartConsumerMap() {
		for(Entry<TopicPartBO /***/, ConsumerOffsetBO> entry : topicPartConsumerMap.entrySet()){
			TopicPartBO partBO = entry.getKey();
			ConsumerOffsetBO offsetBO = entry.getValue();
			log.info("[{}-{}] {}:{}/{}",partBO.getTopic(),partBO.getPartition(),offsetBO.getClientId(),offsetBO.getOffset(),offsetBO.getSize());
		}
	}
	
	public void setTopicService(TopicService topicService) {
		this.topicService = topicService;
	}
	
	public void setBrokerService(BrokerService brokerService) {
		this.brokerService = brokerService;
	}
	
	public void setJmxConnectorService(JMXConnectorService jmxConnectorService) {
		this.jmxConnectorService = jmxConnectorService;
	}
}
