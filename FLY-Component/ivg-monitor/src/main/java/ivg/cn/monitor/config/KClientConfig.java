package ivg.cn.monitor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ivg.cn.kclient.KClientAdmin;

//@Configuration
public class KClientConfig {

	@Bean
	public KClientAdmin clientAdmin() {
		KClientAdmin kAdmin = new KClientAdmin("kafka-adminclient.properties");
		
		return kAdmin;
	}
	
}
