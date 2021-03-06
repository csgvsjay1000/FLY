package ivg.cn.commodity.db;

import java.util.Date;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ivg.cn.commodity.db.dao.CommodityAssertDao;
import ivg.cn.commodity.db.dao.impl.CommodityAssertDaoImpl;
import ivg.cn.commodity.entity.CommodityAssert;

public class CommodityDaoTest {

	@Test
	public void testInsert() {
		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring/spring-content.xml");
		context.start();
		
		CommodityAssertDao assertDao = context.getBean(CommodityAssertDaoImpl.class);
		
		Date date = new Date();
		CommodityAssert assert1 = new CommodityAssert();
		assert1.setCreateDate(date);
		assert1.setUpdateDate(date);
		assert1.setId(System.currentTimeMillis());
		assert1.setBarcode("1234567");
		assert1.setName("怡宝矿泉水");
		assert1.setMerchantId(1234L);
		assert1.setCreater(1L);
		assert1.setUpdater(1L);
		assertDao.insert(date, assert1);
		
	}
	
	@Test
	public void testDBno() {
		
		
		
	}
}
