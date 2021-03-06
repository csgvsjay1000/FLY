package ivg.cn.replenish.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;


@SpringBootApplication
@EnableAutoConfiguration
@DubboComponentScan(basePackages = "ivg.cn.replenish.service")
public class ReplenishServiceStartup 
{
	
	static Logger logger = LoggerFactory.getLogger(ReplenishServiceStartup.class);
	
    public static void main( String[] args )
    {
        SpringApplication.run(ReplenishServiceStartup.class, args);
        logger.info("ReplenishServiceStartup success.");
        try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}