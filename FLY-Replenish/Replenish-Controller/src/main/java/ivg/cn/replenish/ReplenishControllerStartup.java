package ivg.cn.replenish;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;


@SpringBootApplication
@DubboComponentScan(basePackages = "ivg.cn.replenish")
public class ReplenishControllerStartup {
	
	public static void main(String[] args) {
		SpringApplication.run(ReplenishControllerStartup.class, args);
	}
	
}
