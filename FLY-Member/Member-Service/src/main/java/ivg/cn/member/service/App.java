package ivg.cn.member.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@DubboComponentScan(basePackages = "ivg.cn.member.service")
public class App 
{
	
	static Logger logger = LoggerFactory.getLogger(App.class);
	
    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
        logger.info("PrintServiceStartup success.");
    }
}