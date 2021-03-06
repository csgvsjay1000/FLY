package ivg.cn.monitor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ivg.cn.monitor.comp.zk.ZKApi;
import ivg.cn.monitor.comp.zk.ZKConnection;
import ivg.cn.monitor.comp.zk.ZKReactor;
import ivg.cn.monitor.config.bean.ZkBean;

@Configuration
public class ZkConfig {

	@Autowired
	private ZkBean zkBean;
	
	@Bean
	public ZKConnection zkConnection() {
		ZKReactor reactor = new ZKReactor();
		reactor.start();
		ZKConnection conn = new ZKConnection(reactor);
		return conn;
	}
	
	@Bean
	public ZKApi zkApi() {
		ZKApi zkApi = new ZKApi();
		zkApi.openConn(zkBean.getServer());
		return zkApi;
	}
}
