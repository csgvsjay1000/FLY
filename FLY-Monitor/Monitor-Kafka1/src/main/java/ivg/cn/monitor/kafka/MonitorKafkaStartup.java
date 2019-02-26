package ivg.cn.monitor.kafka;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import ivg.cn.dbsplit.LogConfig;

public class MonitorKafkaStartup {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/spring-content-test.xml");
		context.start();
		
		SpringBeanUtil.setContext(context);
		
		MonitorKafkaController controller = new MonitorKafkaController();
		
		controller.start();
		
		LogConfig.getInstance().makeError();
		
	}
	
}
