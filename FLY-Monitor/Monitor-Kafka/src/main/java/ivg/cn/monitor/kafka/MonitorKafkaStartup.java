package ivg.cn.monitor.kafka;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import ivg.cn.monitor.kafka.db.impl.BrokerItemDaoImpl;


/**
 * Hello world!
 *
 */
public class MonitorKafkaStartup 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/spring-content-test.xml");
		context.start();
		
		SpringBeanUtil.setContext(context);
		
		MonitorKafkaController controller = new MonitorKafkaController();
		
		controller.setBrokerItemDao(SpringBeanUtil.getBean(BrokerItemDaoImpl.class));
		
		controller.start();
    }
}
