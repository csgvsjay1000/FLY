//package ivg.cn.monitor.kafka.service;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Date;
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.ThreadFactory;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.atomic.AtomicInteger;
//
//import org.apache.kafka.clients.admin.ConsumerGroupListing;
//
//import ivg.cn.common.ExecutorUtils;
//import ivg.cn.monitor.kafka.AdminClientUtil;
//import ivg.cn.monitor.kafka.SpringBeanUtil;
//import ivg.cn.monitor.kafka.db.TopicDao;
//import ivg.cn.monitor.kafka.db.impl.TopicDaoImpl;
//import ivg.cn.monitor.kafka.entity.Topic;
//import ivg.cn.monitor.kafka.service.BrokerItemService.BrokerConnector;
//import ivg.cn.vesta.IdService;
//
///**
// * 1、定期从BrokerItemService中获取有用的连接，一旦获取成功则停止，当连接失效后，
// *   立即从BrokerItemService中获取其他可用连接，一旦可用连接为空，则又继续开始这定时任务
// * */
//public class TopicService {
//
//	final TopicDao topicDao;
//	IdService idService;
//	
//	final BrokerItemService brokerItemService;
//	
//	volatile BrokerConnector brokerConnector;
//	ScheduledExecutorService brokerConnScheduled;  // 定期获取可用brokerConn
//	ScheduledExecutorService topicSchedule;  // 定期获取topic
//	
//	ConcurrentHashMap<String, Object> topicsMap = new ConcurrentHashMap<>();
//	ConcurrentHashMap<String, Object> groupIdMap = new ConcurrentHashMap<>();
//	ConcurrentHashMap<String/**Consumer-id*/, String> consumerPartMap = new ConcurrentHashMap<>();
//	
//
//	
//	public TopicService(BrokerItemService brokerItemService) {
//		this.brokerItemService = brokerItemService;
//		
//		
//		topicDao = SpringBeanUtil.getBean(TopicDaoImpl.class);
//		idService = SpringBeanUtil.getBean("idService",IdService.class);
//	}
//	
//	public void start() {
//		
//		loadTopicFromDB();
//		
//		restartBrokerConnScheduled();
//		restartTopicSchedule();
//
//	}
//	
//	private void loadTopicFromDB() {
//		List<Topic> topics = topicDao.selectAll(null, Topic.class);
//		if (topics == null || topics.size() == 0) {
//			return;
//		}
//		for (Topic topic : topics) {
//			topicsMap.put(topic.getName(), new Object());
//		}
//	}
//	
//	public void restartBrokerConnScheduled() {
//		if (brokerConnScheduled != null) {
//			brokerConnScheduled.shutdown();
//		}
//		brokerConnScheduled = Executors.newScheduledThreadPool(1, new ThreadFactory() {
//			AtomicInteger index = new AtomicInteger(0);
//			@Override
//			public Thread newThread(Runnable r) {
//				return new Thread(r, "BrokerConnScheduled-"+index.getAndIncrement());
//			}
//		});
//		// 每隔1s中检查是否有可用的连接，若获取到了则停止获取
//		brokerConnScheduled.scheduleWithFixedDelay(obtainEnableBrokerConn(), 5000, 1000, TimeUnit.MILLISECONDS);
//	}
//	
//	public void restartTopicSchedule() {
//		if (topicSchedule != null) {
//			topicSchedule.shutdown();
//		}
//		topicSchedule = ExecutorUtils.namedSingleScheduleExecutor("TopicSchedule");
//		// 每隔5s检查是否有新的topic
//		topicSchedule.scheduleAtFixedRate(obtainTopic(), 5000, 5000, TimeUnit.MILLISECONDS);
//		topicSchedule.scheduleAtFixedRate(listGroupIds(), 5000, 10000, TimeUnit.MILLISECONDS);
//	}
//	
//	public Runnable obtainEnableBrokerConn() {
//		return new Runnable() {
//			
//			@Override
//			public void run() {
//				if (brokerConnector != null) {
//					brokerConnScheduled.shutdown();
//					return;
//				}
//				brokerConnector = brokerItemService.getEnableConn();
//			}
//		};
//	}
//	
//	public Runnable listGroupIds() {
//		return new Runnable() {
//			
//			@Override
//			public void run() {
//				if (brokerConnector == null) {
//					return;
//				}
//				try {
//					Collection<ConsumerGroupListing> consumerGroupListings = AdminClientUtil.listConsumerGroups(brokerConnector);
//					if (consumerGroupListings == null || consumerGroupListings.size() == 0) {
//						return;
//					}
//					for (ConsumerGroupListing consumerGroupListing : consumerGroupListings) {
//						if (!groupIdMap.containsKey(consumerGroupListing.groupId())) {
//							synchronized (consumerGroupListing) {
//								if (!groupIdMap.containsKey(consumerGroupListing.groupId())) {
//									groupIdMap.put(consumerGroupListing.groupId(), new Object());
//								}
//							}
//						}
//					}
//				} catch (InterruptedException | ExecutionException e) {
//					
//					e.printStackTrace();
//				}catch (Exception e) {
//					e.printStackTrace();
//				}
//				
//			}
//		};
//	}
//	
//	private Runnable ob() {
//		return new Runnable() {
//			
//			@Override
//			public void run() {
//				if (brokerConnector == null) {
//					return;
//				}
//				
//			}
//		};
//	}
//	
//	public Runnable obtainTopic() {
//		return new Runnable() {
//			
//			@Override
//			public void run() {
//				if (brokerConnector == null) {
//					return;
//				}
//				try {
//					Set<String> topics = AdminClientUtil.listTopic(brokerConnector);
//					if (topics != null && topics.size()>0) {
//						List<String> newAddtopicList = new ArrayList<>();
//						for (String top : topics) {
//							if (!topicsMap.containsKey(top)) {
//								topicsMap.put(top, new Object());
//								newAddtopicList.add(top);
//							}
//						}
//						if (newAddtopicList.size()>0) {
//							// 将新获取的数据插入数据库
//							List<Topic> topicsInsert = new ArrayList<>(newAddtopicList.size());
//							for (int i = 0; i < newAddtopicList.size(); i++) {
//								Topic topic = new Topic();
//								topic.setName(newAddtopicList.get(i));
//								topic.setId(idService.genId());
//								Date date = new Date();
//								topic.setCreateDate(date);
//								topic.setUpdateDate(date);
//								topic.setCreater(1L);
//								topic.setUpdater(1L);
//								topicsInsert.add(topic);
//							}
//							topicDao.batchInsert(null, topicsInsert);
//						}
//					}
//				} catch (InterruptedException | ExecutionException e) {
//					e.printStackTrace();
//				}catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		};
//	}
//	
//	
//}
