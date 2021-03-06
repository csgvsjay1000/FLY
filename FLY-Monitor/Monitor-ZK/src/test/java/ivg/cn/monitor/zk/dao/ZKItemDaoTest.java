package ivg.cn.monitor.zk.dao;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ivg.cn.monitor.zk.db.ZKItemDao;
import ivg.cn.monitor.zk.db.ZkInstMonitorDao;
import ivg.cn.monitor.zk.db.impl.ZKItemDaoImpl;
import ivg.cn.monitor.zk.db.impl.ZkInstMonitorDaoImpl;
import ivg.cn.monitor.zk.entity.ZkInstMonitor;
import ivg.cn.monitor.zk.entity.ZkItem;

public class ZKItemDaoTest {

	ClassPathXmlApplicationContext context;
	
	@Before
	public void beforeZK() {
		context = new ClassPathXmlApplicationContext("classpath*:spring/spring-content-test.xml");
		context.start();
	}
	
	@After
	public void afterZK() {
		context.close();
	}
	
	@Test
	public void testSelect() {
		ZKItemDao zkItemDao = context.getBean(ZKItemDaoImpl.class);
		List<ZkItem> items = zkItemDao.selectAll(null, ZkItem.class);
		for (ZkItem zkItem : items) {
			System.out.println(zkItem);
		}
	}
	
	@Test
	public void testInsert() {
		ZkInstMonitorDao instMonitorDao = context.getBean(ZkInstMonitorDaoImpl.class);
		
		ZkInstMonitor instMonitor = new ZkInstMonitor();
		instMonitor.setId(2L);
		
		instMonitorDao.insert(null, instMonitor);
	}
	
}
