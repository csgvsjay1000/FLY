package ivg.cn.boss.service;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ivg.cn.boss.api.MerchantService;
import ivg.cn.common.DreamResponse;

public class ConsumerTest {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring/boss-db-consumer.xml");
		context.start();
		
		MerchantService merchantService = (MerchantService) context.getBean("merchantService");
		
	}
	
}
