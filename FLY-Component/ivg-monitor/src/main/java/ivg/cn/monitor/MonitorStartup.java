package ivg.cn.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;

import ivg.cn.monitor.comp.dubbo.RegistryContainer;

@SpringBootApplication
//@DubboComponentScan(basePackages = "ivg.cn.monitor.comp.dubbo")
public class MonitorStartup {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(MonitorStartup.class, args);
		
		/*RegistryContainer registryContainer = context.getBean(RegistryContainer.class);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		registryContainer.start();*/
	}
	
}
