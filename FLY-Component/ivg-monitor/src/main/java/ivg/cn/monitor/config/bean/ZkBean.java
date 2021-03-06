package ivg.cn.monitor.config.bean;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties
@PropertySource(value = { "classpath:zk.properties" })
public class ZkBean {

	String server;

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}
	
}
