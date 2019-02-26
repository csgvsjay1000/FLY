package ivg.cn.monitor.kafka;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import ivg.cn.common.ExecutorUtils;
import ivg.cn.monitor.kafka.service.BrokerService;
import ivg.cn.monitor.kafka.service.TopicService_2;

public class MonitorKafkaController {

	BrokerService brokerService;
	TopicService_2 topicService;
	
	// 全局定时任务
	ScheduledExecutorService scheduledService;
	ThreadPoolExecutor publicExecutor = ExecutorUtils.traceFixedThreadPool("PublicExecutor", 10);
	
	
	
	public MonitorKafkaController() {
		brokerService = new BrokerService(this);
		topicService = new TopicService_2(this);
		
		scheduledService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
			
			AtomicInteger index = new AtomicInteger(0);
			
			@Override
			public Thread newThread(Runnable r) {
				
				return new Thread(r, "ScheduledService-"+index.getAndIncrement());
			}
		});
		
	}
	
	public void start() {
		brokerService.start();
		topicService.start();
	}
	
	public ScheduledExecutorService getScheduledService() {
		return scheduledService;
	}
	
	public ThreadPoolExecutor getPublicExecutor() {
		return publicExecutor;
	}
	
	public BrokerService getBrokerService() {
		return brokerService;
	}
}
