package ivg.cn.monitor.kafka.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.management.remote.JMXConnector;

import org.apache.kafka.clients.admin.AdminClient;

import ivg.cn.common.UtilAll;
import ivg.cn.common.UtilAll.MapRemove;
import ivg.cn.monitor.kafka.AdminClientUtil;
import ivg.cn.monitor.kafka.JmxUtil;
import ivg.cn.monitor.kafka.MonitorKafkaController;
import ivg.cn.monitor.kafka.SpringBeanUtil;
import ivg.cn.monitor.kafka.bo.BrokerItemBean;
import ivg.cn.monitor.kafka.db.BrokerItemDao;
import ivg.cn.monitor.kafka.db.impl.BrokerItemDaoImpl;
import ivg.cn.monitor.kafka.entity.BrokerItem;
import ivg.cn.monitor.kafka.bo.BrokerConnector;

public class BrokerService {

	MonitorKafkaController kafkaController;
	BrokerItemDao brokerItemDao;
	
	ConcurrentHashMap<BrokerItemBean, BrokerConnector> validConnMap = new ConcurrentHashMap<>();  // 有效连接
	ConcurrentHashMap<BrokerItemBean, BrokerConnector> unvalidConnMap = new ConcurrentHashMap<>();  // 无效连接
	int brokerItemInterval = 2000;  // 每隔10更新一次brokerItem

	
	public BrokerService(MonitorKafkaController kafkaController) {
		this.kafkaController = kafkaController;
		brokerItemDao = SpringBeanUtil.getBean(BrokerItemDaoImpl.class);

	}
	
	public void start() {
		ScheduledExecutorService scheduler = kafkaController.getScheduledService();
		scheduler.scheduleWithFixedDelay(listBroker(), 5000, brokerItemInterval, TimeUnit.MILLISECONDS);
	}
	
	public Runnable listBroker() {
		return new Runnable() {
		
			@Override
			public void run() {
				kafkaController.getPublicExecutor().submit(listBrokerImpl());
			}
		};
	}

	/**
	 * 定时从数据库获取最新的记录，若增加了或修改了数据库记录，及时更新连接状态
	 * 1、从数据库获取记录
	 * */
	public Runnable listBrokerImpl() {
		return new Runnable() {
			
			@Override
			public void run() {
				List<BrokerItem> brokerItems = brokerItemDao.selectAll(null, BrokerItem.class);
				if (brokerItems != null) {
					// 1、先判断是否为无效的，若无效连接则，更新无效连接时间
					// 2、若不在无效连接里
					// 3、判断是否在有效连接里，在有效连接的更新时间
					// 4、若不在有效连接里，则放入有效连接
					for (BrokerItem brokerItem : brokerItems) {
						BrokerItemBean itemBean = BrokerItemBean.create(brokerItem);
						if (unvalidConnMap.containsKey(itemBean)) {
							unvalidConnMap.get(itemBean).update();
						}else {
							if (validConnMap.containsKey(itemBean)) {
								validConnMap.get(itemBean).update();
							}else {
								BrokerConnector connector = createConnector(itemBean);
								if (connector != null) {
									validConnMap.putIfAbsent(itemBean, connector);
								}else {
									// 无效连接
									connector = new BrokerConnector();
									connector.setValid(false);
									connector.update();
									unvalidConnMap.putIfAbsent(itemBean, connector);
								}
							}
						}
					}
				}
				
				iterMapAndRemove(validConnMap, brokerItemInterval*3);
				iterMapAndRemove(unvalidConnMap, brokerItemInterval*3);
				
				System.out.println("======================================");
				System.out.println("validConnMap = "+validConnMap.keySet());
				System.out.println("unvalidConnMap = "+unvalidConnMap.keySet());
				System.out.println();
			}
		};
	}
	
	private BrokerConnector createConnector(BrokerItemBean itemBean) {
		JMXConnector connector = 
		JmxUtil.createConnector(itemBean.getBrokerIp()+":"+itemBean.getJmxPort());
		if (connector != null) {
			AdminClient adminClient = AdminClientUtil.createAdminClient(itemBean.getBrokerIp()+":"+itemBean.getBrokerPort());
			BrokerConnector brokerConnector = new BrokerConnector(connector, adminClient,itemBean);
			brokerConnector.update();
			return brokerConnector;
		}
		return null;
	}
	
	private void iterMapAndRemove(Map<BrokerItemBean,BrokerConnector> map, int timeoutMills) {
		UtilAll.mapRemoveItem(map, timeoutMills, new MapRemove<BrokerItemBean, BrokerConnector>() {

			@Override
			public void operation(Entry<BrokerItemBean, BrokerConnector> entry,
					Iterator<Entry<BrokerItemBean, BrokerConnector>> iter) {
				
				if (entry.getValue().expire(timeoutMills)) {
					// 超过3次检测到数据库没有该条记录，即认为这条记录被删除
					entry.getValue().close();
					iter.remove();
				}
			}
		});
	}
	
	public BrokerConnector getEnableConn() {
		if (validConnMap.size() > 0) {
			return validConnMap.values().iterator().next();
		}
		return null;
	}
	
	/** 标记为无效连接 */
	public void markBrokerConnectorUnvaild(BrokerConnector connector) {
		// 1、将次连接由有效迁移至无效
		// 2、先判断无效连接中是否存在，若存在，则无效连接中不用操作,若不存在，则放置进去
		// 3、若有效连接中存在，则移除
		if (connector == null) {
			return;
		}
		if (connector.isValid()) {
			connector.setValid(false);
			connector.close();
		}
		if (!unvalidConnMap.containsKey(connector.getKey())) {
			unvalidConnMap.putIfAbsent(connector.getKey(), connector);
		}
		
		if (validConnMap.containsKey(connector.getKey())) {
			validConnMap.remove(connector.getKey());
		}
		
	}
	
}
