package ivg.cn.replenish.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;

import ivg.cn.replenish.service.config.bean.DubboBean;


@Configuration
public class DubboConfiguration {

	@Autowired
	private DubboBean dubboBean;
	
	@Bean
    public ApplicationConfig applicationConfig() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(dubboBean.getName());
        return applicationConfig;
    }

    @Bean
    public RegistryConfig registryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(dubboBean.getRegistryAddress());
        registryConfig.setClient("curator");
        registryConfig.setProtocol("zookeeper");
        return registryConfig;
    }
    
    @Bean
    public ProtocolConfig protocolConfig() {
    	ProtocolConfig protocolConfig = new ProtocolConfig();
    	
    	protocolConfig.setPort(dubboBean.getPort());
    	
    	return protocolConfig;
	}
}
