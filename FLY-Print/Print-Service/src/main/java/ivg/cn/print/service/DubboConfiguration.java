package ivg.cn.print.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;

import ivg.cn.print.service.config.bean.DubboBean;

@Configuration
public class DubboConfiguration {

	@Autowired
	private DubboBean dubboBean;
	
	@Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("print-service");
        return applicationConfig;
    }

    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress("zookeeper://192.168.5.131:16481");
        registryConfig.setClient("curator");
        registryConfig.setProtocol("zookeeper");
//        registryConfig.setAddress("multicast://224.5.6.7:1234");
        return registryConfig;
    }
    
    @Bean
    public ProtocolConfig protocolConfig() {
    	ProtocolConfig protocolConfig = new ProtocolConfig();
    	
    	protocolConfig.setPort(dubboBean.getPort());
    	
    	return protocolConfig;
	}
}
