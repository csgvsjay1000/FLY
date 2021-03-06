package ivg.cn.boss.db.dao;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ivg.cn.boss.api.MerchantService;
import ivg.cn.boss.dto.InsertMerchantDTO;

public class MerchantDaoTest {

	@Test
	public void testInsert() {
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("/spring/spring-content.xml");
//		context.start();
		MerchantService merchantService = (MerchantService) context.getBean("merchantService");
		
		InsertMerchantDTO merchantDTO = new InsertMerchantDTO();
		merchantDTO.setName("mjstyle");
		merchantDTO.setUsername("mjstyle_admin");
		merchantDTO.setPassword("123456");
		merchantService.insertMerchant(merchantDTO);
	}
	
	@Test
	public void testDelete() {
		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring/spring-content.xml");
		context.start();
		MerchantService mm = (MerchantService) context.getBean("merchantService");
		
//		merchantService.delete(1540346951146L, 1540346951146L, BossMerchant.class);
		mm.deleteMerchant(1540346951146L);
	}
	
}
