package ivg.cn.merchant.db;

import java.util.Date;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ivg.cn.merchant.api.MerchantUserService;
import ivg.cn.merchant.entity.MerchantUser;

public class MerchantUserServiceTest {

	
	@Test
	public void testInsert() {
		
		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring/spring-content.xml");
		context.start();
		
		MerchantUserService merchantUserService = (MerchantUserService) context.getBean("merchantUserService");
		
		Date date = new Date();
		
		MerchantUser merchantUser = new MerchantUser();
		merchantUser.setId(1234);
		merchantUser.setUsername("alibaba");
		merchantUser.setPassword("123456");
		merchantUser.setAliasName("阿里巴巴");
		merchantUser.setCreateDate(date);
		merchantUser.setUpdateDate(date);
		merchantUser.setMerchantId(1);
		merchantUserService.insert(merchantUser);
		
		System.out.println(merchantUser.getAliasName());
		
	}
	
}
