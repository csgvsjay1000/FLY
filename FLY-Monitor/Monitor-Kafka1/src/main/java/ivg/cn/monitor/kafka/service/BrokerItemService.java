//package ivg.cn.monitor.kafka.service;
//
//import java.io.IOException;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//import javax.management.remote.JMXConnector;
//
//import org.apache.kafka.clients.admin.AdminClient;
//
//import ivg.cn.monitor.kafka.AdminClientUtil;
//import ivg.cn.monitor.kafka.JmxUtil;
//import ivg.cn.monitor.kafka.MonitorKafkaController;
//import ivg.cn.monitor.kafka.SpringBeanUtil;
//import ivg.cn.monitor.kafka.bo.BrokerItemBean;
//import ivg.cn.monitor.kafka.bo.ExpireToDelete;
//import ivg.cn.monitor.kafka.db.BrokerItemDao;
//import ivg.cn.monitor.kafka.db.impl.BrokerItemDaoImpl;
//import ivg.cn.monitor.kafka.entity.BrokerItem;
//
///**
// * 负责从数据库中获取broker列表，连接broker并维护
// * */
//public class BrokerItemService {
//	
//	BrokerItemDao brokerItemDao;
//	
//	MonitorKafkaController kafkaController;
//	
//	ConcurrentHashMap<BrokerItemBean, BrokerConnector> validConnMap = new ConcurrentHashMap<>();  // 有效连接
//	ConcurrentHashMap<BrokerItemBean, BrokerConnector> unvalidConnMap = new ConcurrentHashMap<>();  // 无效连接
//	
//	int brokerItemInterval = 2000;  // 每隔10更新一次brokerItem
//	
//	public BrokerItemService(MonitorKafkaController kafkaController) {
//		brokerItemDao = SpringBeanUtil.getBean(BrokerItemDaoImpl.class);
//		this.kafkaController = kafkaController;
//		
//	}
//
//	public void start() {
//		
//		ScheduledExecutorService scheduler = kafkaController.getScheduledService();
//		
//		// 每隔10s从数据库更新broker列表
//		scheduler.scheduleWithFixedDelay(listBrokerItem(), 5000, brokerItemInterval, TimeUnit.MILLISECONDS);
//		
//	}
//	
//	public Runnable listBrokerItem() {
//		return new Runnable() {
//			
//			@Override
//			public void run() {
//				kafkaController.getPublicExecutor().submit(listBrokerItemImpl());
//			}
//		};
//	}
//	
//	public Runnable listBrokerItemImpl() {
//		return new Runnable() {
//			
//			@Override
//			public void run() {
//				List<BrokerItem> brokerItems = brokerItemDao.selectAll(null, BrokerItem.class);
//				if (brokerItems != null) {
//					for (BrokerItem brokerItem : brokerItems) {
//						BrokerItemBean itemBean = BrokerItemBean.create(brokerItem);
//						if (unvalidConnMap.containsKey(itemBean)) {
//							unvalidConnMap.get(itemBean).update();
//							
//							if (validConnMap.containsKey(itemBean)) {
//								if (validConnMap.get(itemBean).isValid()) {
//									validConnMap.get(itemBean).setValid(false);
//									validConnMap.get(itemBean).close();
//								}
//								validConnMap.remove(itemBean);
//							}
//							
//							continue;
//						}
//						if (!validConnMap.containsKey(itemBean)) {
//							JMXConnector connector = 
//									JmxUtil.createConnector(itemBean.getBrokerIp()+":"+itemBean.getJmxPort());
//							if (connector != null) {
//								AdminClient adminClient = AdminClientUtil.createAdminClient(itemBean.getBrokerIp()+":"+itemBean.getBrokerPort());
//								BrokerConnector brokerConnector = new BrokerConnector(connector, adminClient,itemBean);
//								brokerConnector.update();
//								validConnMap.putIfAbsent(itemBean, brokerConnector);
//							}
//						}else {
//							validConnMap.get(itemBean).update();
//						}
//						
//					}
//				}
//				
//				// 同时定期去掉数据库已删除的记录
//				Iterator<Map.Entry<BrokerItemBean, BrokerConnector>> iter = validConnMap.entrySet().iterator();
//				while (iter.hasNext()) {
//					Map.Entry<BrokerItemBean, BrokerConnector> entry = iter.next();
//					if (entry.getValue().expire(brokerItemInterval*3)) {
//						// 超过3次检测到数据库没有该条记录，即认为这条记录被删除
//						entry.getValue().close();
//						iter.remove();
//					}
//				}
//				
//				// 同时定期去掉数据库已删除的记录
//				Iterator<Map.Entry<BrokerItemBean, BrokerConnector>> iter1 = unvalidConnMap.entrySet().iterator();
//				while (iter1.hasNext()) {
//					Map.Entry<BrokerItemBean, BrokerConnector> entry = iter1.next();
//					if (entry.getValue().expire(brokerItemInterval*3)) {
//						// 超过3次检测到数据库没有该条记录，即认为这条记录被删除
//						entry.getValue().close();
//						iter1.remove();
//					}
//				}
//				System.out.println("valid = "+validConnMap.keySet());
//				System.out.println("unvalid ="+unvalidConnMap.keySet());
//			}
//		};
//	}
//	
//	
//	public BrokerItemService.BrokerConnector getEnableConn() {
//		if (validConnMap.size() > 0) {
//			return validConnMap.values().iterator().next();
//		}
//		return null;
//	}
//	
//	public void markBrokerConnector(BrokerConnector connector,boolean enable) {
//		if (!enable) {
//			// 将有效的标记为无效
//			if (connector.isValid()) {
//				connector.setValid(false);
//				validConnMap.remove(connector.getKey());
//				if (!unvalidConnMap.containsKey(connector.getKey())) {
//					connector.update();
//					unvalidConnMap.putIfAbsent(connector.getKey(), connector);
//				}
//			}
//		}else {
//			
//		}
//	}
//	
//	
//	public static class BrokerConnector extends ExpireToDelete{
//		private final JMXConnector jmxConnector;
//		
//		private final AdminClient adminClient;
//		
//		private volatile boolean valid = true;  //有效无效 
//		
//		private BrokerItemBean key;
//
//		public BrokerConnector(JMXConnector jmxConnector, AdminClient adminClient,BrokerItemBean key) {
//			super();
//			this.jmxConnector = jmxConnector;
//			this.adminClient = adminClient;
//			this.key = key;
//			
//		}
//		
//		public void setValid(boolean valid) {
//			this.valid = valid;
//		}
//		
//		public boolean isValid() {
//			return valid;
//		}
//		
//		public JMXConnector getJmxConnector() {
//			return jmxConnector;
//		}
//		
//		public AdminClient getAdminClient() {
//			return adminClient;
//		}
//		
//		public BrokerItemBean getKey() {
//			return key;
//		}
//		
//		public void close() {
//			if (adminClient != null) {
//				adminClient.close();
//			}
//			if (jmxConnector != null) {
//				try {
//					jmxConnector.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//	}
//	
//}
