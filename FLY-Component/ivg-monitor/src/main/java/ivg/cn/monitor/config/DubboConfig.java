package ivg.cn.monitor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.registry.RegistryService;

//@Configuration
public class DubboConfig {
	@Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("provider-test");
        return applicationConfig;
    }

    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        /*registryConfig.setAddress("zookeeper://127.0.0.1:2181");
        registryConfig.setClient("zkclient");*/
        registryConfig.setAddress("zookeeper://192.168.5.131:16481");
        registryConfig.setClient("zkclient");
//        registryConfig.setAddress("multicast://224.5.6.7:1234");
        return registryConfig;
    }
    
    @Bean
    public ProtocolConfig protocolConfig() {
    	ProtocolConfig protocolConfig = new ProtocolConfig();
    	
    	protocolConfig.setPort(3452);
    	
    	return protocolConfig;
	}
    
}
