package ivg.cn.monitor.kafka.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.admin.DescribeConsumerGroupsOptions;
import org.apache.kafka.clients.admin.DescribeConsumerGroupsResult;
import org.apache.kafka.clients.admin.ListConsumerGroupsOptions;
import org.apache.kafka.clients.admin.ListConsumerGroupsResult;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.MemberDescription;
import org.apache.kafka.common.KafkaFuture;

import ivg.cn.common.ExecutorUtils;
import ivg.cn.common.UtilAll;
import ivg.cn.common.UtilAll.MapRemove;
import ivg.cn.monitor.kafka.MonitorKafkaController;
import ivg.cn.monitor.kafka.bo.BrokerConnector;
import ivg.cn.monitor.kafka.bo.ExpireToDelete;

public class TopicService_2 {

	MonitorKafkaController kafkaController;
	
	volatile BrokerConnector brokerConnector;
	AtomicInteger index = new AtomicInteger(0);
	volatile ScheduledExecutorService brokerConnScheduled;  // 定期获取可用brokerConn
	ConcurrentHashMap<String, ExpireToDelete> groupMap = new ConcurrentHashMap<>();
	ConcurrentHashMap<String, ExpireToDelete> topicMap = new ConcurrentHashMap<>();
	
	ThreadPoolExecutor poolExecutor = ExecutorUtils.traceFixedThreadPool("TopicService", 10);
	
	int groupIdInterval = 2000;  // 定时获取groupId时间
	AtomicBoolean restartConnScheduled = new AtomicBoolean(false);

	
	public TopicService_2(MonitorKafkaController kafkaController) {
		this.kafkaController = kafkaController;
	}
	
	public void start() {
		
		restartBrokerScheduler();
		
		ScheduledExecutorService scheduler = kafkaController.getScheduledService();
		scheduler.scheduleAtFixedRate(groupIds(), 5000, groupIdInterval, TimeUnit.MILLISECONDS);
		scheduler.scheduleAtFixedRate(topics(), 5000, groupIdInterval, TimeUnit.MILLISECONDS);
	}
	
	public void restartBrokerScheduler() {
		if (!restartConnScheduled.compareAndSet(false, true)) {
			return;
		}
		try {
			if (brokerConnScheduled != null) {
				return;
			}
			brokerConnScheduled = Executors.newScheduledThreadPool(1, new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					return new Thread(r, "BrokerConnScheduled-"+index.getAndIncrement());
				}
			});
			// 每隔1s中检查是否有可用的连接，若获取到了则停止获取
			brokerConnScheduled.scheduleWithFixedDelay(enableConnector(), 5000, 1000, TimeUnit.MILLISECONDS);
		} finally {
			restartConnScheduled.set(false);
		}
		
	}
	
	public Runnable enableConnector() {
		return new Runnable() {
			
			@Override
			public void run() {
				if (getBrokerConnector() != null) {
					brokerConnScheduled.shutdown();
					brokerConnScheduled = null;
					return;
				}
				brokerConnector = kafkaController.getBrokerService().getEnableConn();
			}
		};
	}
	
	
	public BrokerConnector getBrokerConnector() {
		if (brokerConnector != null && brokerConnector.isValid()) {
			return brokerConnector;
		}
		return null;
	}
	
	public Runnable groupIds() {
		return new Runnable() {
			
			@Override
			public void run() {
				poolExecutor.submit(groupIdsImpl());
			}
		};
	}
	
	public Runnable topics() {
		return new Runnable() {
			
			@Override
			public void run() {
				poolExecutor.submit(topicsImpl());
			}
		};
	}
	
	private Runnable groupIdsImpl() {
		return new Runnable() {
			
			@Override
			public void run() {
				final BrokerConnector connector = getBrokerConnector();
				if (connector != null) {
					ListConsumerGroupsOptions options = new ListConsumerGroupsOptions();
					options.timeoutMs(3000);  // 3s没有回应
					ListConsumerGroupsResult groupsResult = connector.getAdminClient().listConsumerGroups(options);
					try {
						Collection<Throwable> throwables = groupsResult.errors().get();
						if (throwables != null && throwables.size()>0) {
							if (connector.exceedFaildTimeLimitAndInc(3)) {
								// 是否超过3次失败了，超过就清楚
								System.out.println("the connector closed.");
								reTakeConnector(connector);
								return;
							}
						}
						
						Collection<ConsumerGroupListing> groupListings = groupsResult.valid().get();
						if (groupListings != null) {
							for (ConsumerGroupListing consumerGroupListing : groupListings) {
								if (!groupMap.containsKey(consumerGroupListing.groupId())) {
									groupMap.putIfAbsent(consumerGroupListing.groupId(), new ExpireToDelete().update());
								}else {
									groupMap.get(consumerGroupListing.groupId()).update();
								}
							}
						}
						
					} catch (InterruptedException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				iterMapAndRemove(groupMap,groupIdInterval*3);
				System.out.println("groupMap = "+groupMap.keySet());
					
			}
		};
	}
	
	private Runnable topicsImpl() {
		return new Runnable() {
			
			@Override
			public void run() {
				final BrokerConnector connector = getBrokerConnector();
				if (connector != null) {
					ListTopicsOptions options = new ListTopicsOptions();
					options.timeoutMs(3000);  // 3s没有回应
					ListTopicsResult groupsResult = connector.getAdminClient().listTopics(options);
					try {
						
						Set<String> topicNames = groupsResult.names().get();
						if (topicNames != null) {
							for (String topic : topicNames) {
								if (!topicMap.containsKey(topic)) {
									topicMap.putIfAbsent(topic, new ExpireToDelete().update());
								}else {
									topicMap.get(topic).update();
								}
							}
						}
						
					} catch (InterruptedException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						if (connector.exceedFaildTimeLimitAndInc(3)) {
							reTakeConnector(connector);
						}
					}
				}
				iterMapAndRemove(topicMap,groupIdInterval*3);
				System.out.println("topicMap = "+topicMap.keySet());
				
			}
		};
	}
	
	private Runnable topicOfConsumerImpl() {
		return new Runnable() {
			
			@Override
			public void run() {
				final BrokerConnector connector = getBrokerConnector();
				if (connector != null) {
					DescribeConsumerGroupsOptions options = new DescribeConsumerGroupsOptions();
					options.timeoutMs(3000);  // 3s没有回应
					DescribeConsumerGroupsResult groupsResult = connector.getAdminClient().describeConsumerGroups(groupMap.keySet(),options);
					try {
						
						Map<String, KafkaFuture<ConsumerGroupDescription>> consumerGroupDescriptionMap = groupsResult.describedGroups();
						for(KafkaFuture<ConsumerGroupDescription> decs : consumerGroupDescriptionMap.values()){
							ConsumerGroupDescription groupDescription = decs.get();
							Collection<MemberDescription> memberDescriptions = groupDescription.members();
							for (MemberDescription memberDescription : memberDescriptions) {
								
							}
						}
						
					} catch (InterruptedException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						if (connector.exceedFaildTimeLimitAndInc(3)) {
							reTakeConnector(connector);
						}
					}
				}
				iterMapAndRemove(topicMap,groupIdInterval*3);
				System.out.println("topicMap = "+topicMap.keySet());
				
			}
		};
	}
	
	private void iterMapAndRemove(Map<String,ExpireToDelete> map, int timeoutMills) {
		UtilAll.mapRemoveItem(map, timeoutMills, new MapRemove<String, ExpireToDelete>() {

			@Override
			public void operation(Entry<String, ExpireToDelete> entry,
					Iterator<Entry<String, ExpireToDelete>> iter) {
				
				if (entry.getValue().expire(timeoutMills)) {
					// 超过3次检测到数据库没有该条记录，即认为这条记录被删除
					
					iter.remove();
				}
			}
		});
	}
	
	private void reTakeConnector(BrokerConnector connector) {
		TopicService_2.this.kafkaController.getBrokerService().markBrokerConnectorUnvaild(connector);
		restartBrokerScheduler();
	}
	
}
