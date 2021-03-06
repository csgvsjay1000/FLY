package ivg.cn.monitor.zk;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import ivg.cn.monitor.zk.db.ZKItemDao;
import ivg.cn.monitor.zk.db.ZkInstMonitorDao;
import ivg.cn.monitor.zk.db.impl.ZKItemDaoImpl;
import ivg.cn.monitor.zk.db.impl.ZkInstMonitorDaoImpl;
import ivg.cn.vesta.IdService;

public class MonitorZkStartup {

	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/spring-content-test.xml");
		context.start();
		
		MonitorZkController zkController = new MonitorZkController();

		ZKItemDao zkItemDao = context.getBean(ZKItemDaoImpl.class);
		zkController.setZkItemDao(zkItemDao);
		ZkInstMonitorDao instMonitorDao = context.getBean(ZkInstMonitorDaoImpl.class);
		zkController.setInstMonitorDao(instMonitorDao);
		IdService idService = (IdService) context.getBean("idService");
		zkController.setIdService(idService);
		
		zkController.start();
		
	}
	
}
