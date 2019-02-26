package ivg.cn.commodity.db;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CommodityDBStartup {
	
	public static void main(String[] args) {
		
		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring/spring-content.xml");
		context.start();
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
