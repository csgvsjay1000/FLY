package ivg.cn.boss.db;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class BossDBStartup {

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
