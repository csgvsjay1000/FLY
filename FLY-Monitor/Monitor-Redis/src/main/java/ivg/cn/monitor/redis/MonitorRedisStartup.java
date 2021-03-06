package ivg.cn.monitor.redis;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import ivg.cn.dbsplit.LogConfig;
import ivg.cn.monitor.redis.db.RedisInstMonitorDao;
import ivg.cn.monitor.redis.db.RedisItemDao;
import ivg.cn.monitor.redis.db.impl.RedisInstMonitorDaoImpl;
import ivg.cn.monitor.redis.db.impl.RedisItemDaoImpl;
import ivg.cn.monitor.redis.util.SpringBeanUtil;
import ivg.cn.vesta.IdService;

public class MonitorRedisStartup {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/spring-content-test.xml");
		context.start();
		
		SpringBeanUtil.setContext(context);
		
		MonitorRedisController redisController = new MonitorRedisController();

		IdService idService = (IdService) context.getBean("idService");
		redisController.setIdService(idService);
		
		RedisItemDao redisItemDao = context.getBean(RedisItemDaoImpl.class);
		redisController.setRedisItemDao(redisItemDao);
		
		RedisInstMonitorDao instMonitorDao = context.getBean(RedisInstMonitorDaoImpl.class);
		redisController.setRedisInstMonitorDao(instMonitorDao);
		
		redisController.start();
		LogConfig.getInstance().makeError();
	}
	
}
