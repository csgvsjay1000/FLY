package ivg.cn.monitor.kafka;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;

import ivg.cn.common.ExecutorUtils;
import ivg.cn.monitor.kafka.db.BrokerItemDao;
import ivg.cn.monitor.kafka.entity.BrokerItem;
import ivg.cn.monitor.kafka.service.BrokerService;

/**
 * <pre>
 * 第一、有个单线程池定时任务检查broker_item中是否有新的broker加入，有则添加到监控列表中
 * 第二、有个定时任务2，定时依次获取监控列表中的broker列表，新建每个获取broker的jmx任务，并提交到数据库中
 * 
 * 
 * */
public class MonitorKafkaController {

	
	BrokerItemDao brokerItemDao;
	ScheduledExecutorService listDbBrokerItemScheduled;
	
	ScheduledExecutorService brokerJmxScheduled;
	ExecutorService brokerJmxService = ExecutorUtils.traceFixedThreadPool("BrokerJmxService", 20);
	
	Map<String/**ipAndPort*/, BrokerConnector> jmxConnectorTable = new HashMap<>();
	
	BrokerService brokerService;
	
	public MonitorKafkaController() {
		brokerService = new BrokerService(this);
	}
	
	public void start() {
		new Thread(){
			@Override
			public void run() {
				restartListDbBrokerItemTask();
			}
		}.start();
		new Thread(){
			@Override
			public void run() {
				restartBrokerJmxScheduled();
			}
		}.start();
		
		brokerService.start();
	}
	
	public void restartListDbBrokerItemTask() {
		if (listDbBrokerItemScheduled != null) {
			listDbBrokerItemScheduled.shutdown();
		}
		listDbBrokerItemScheduled = ExecutorUtils.namedSingleScheduleExecutor("listDbBrokerItemScheduled");
		ScheduledFuture<?> future = listDbBrokerItemScheduled.scheduleAtFixedRate(scanBrokerItems(), 2000,30000, TimeUnit.MILLISECONDS);
		try {
			future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			restartListDbBrokerItemTask();
		}
	}
	
	public void restartBrokerJmxScheduled() {
		if (brokerJmxScheduled != null) {
			brokerJmxScheduled.shutdown();
		}
		brokerJmxScheduled = ExecutorUtils.namedSingleScheduleExecutor("brokerJmxScheduled");
		ScheduledFuture<?> future = brokerJmxScheduled.scheduleAtFixedRate(iteratorJmxConnectorTable(), 5000, 5000, TimeUnit.MILLISECONDS);
		try {
			future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			restartBrokerJmxScheduled();
		}
	}
	
	public Runnable scanBrokerItems() {
		return new Runnable() {
			
			@Override
			public void run() {
				List<BrokerItem> brokerItems = brokerItemDao.selectAll(null, BrokerItem.class);
				if (brokerItems == null) {
					return;
				}
				for (BrokerItem brokerItem : brokerItems) {
					String ipAndPort = brokerItem.getBrokerIp()+":"+brokerItem.getJmxPort();
					
					BrokerConnector brokerConnector = jmxConnectorTable.get(ipAndPort);
					
					
					
					if (brokerConnector == null) {
						String jmxUrl = "service:jmx:rmi:///jndi/rmi://" +ipAndPort+ "/jmxrmi";
						
						try {
							JMXServiceURL jmxServiceURL = new JMXServiceURL(jmxUrl);
							JMXConnector connector = JMXConnectorFactory.connect(jmxServiceURL, null);
							if (connector == null ) {
								return;
							}
							
							Properties properties = new Properties();
							String url = brokerItem.getBrokerIp()+":"+brokerItem.getBrokerPort();
							properties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, url);
							AdminClient adminClient = AdminClient.create(properties);
							
							if (adminClient == null) {
								connector.close();
							}
							brokerConnector = new BrokerConnector(connector, adminClient);
							jmxConnectorTable.put(ipAndPort, brokerConnector);
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				}
			}
		};
	}
	
	public Runnable iteratorJmxConnectorTable() {
		return new Runnable() {
			
			@Override
			public void run() {
				for(Map.Entry<String/**ipAndPort*/, BrokerConnector> entry : jmxConnectorTable.entrySet()){
					try {
						MBeanServerConnection connection = entry.getValue().getJmxConnector().getMBeanServerConnection();
						BrokerJmxUtil.byteInPerSec(connection);
					} catch (IOException e) {
						e.printStackTrace();
					}
					AdminClientUtil.listTopics(entry.getValue(), brokerService);
				}
			}
		};
		
	}
	
	public void submitTask(Runnable task) {
		brokerJmxService.execute(task);
	}
	
	public void setBrokerItemDao(BrokerItemDao brokerItemDao) {
		this.brokerItemDao = brokerItemDao;
	}
	
}
