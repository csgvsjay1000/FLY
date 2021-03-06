package ivg.cn.print.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;

/**
 * Hello world!
 *
 */

@SpringBootApplication
@EnableAutoConfiguration
@DubboComponentScan(basePackages = "ivg.cn.print.service")
public class PrintServiceStartup 
{
	
	static Logger logger = LoggerFactory.getLogger(PrintServiceStartup.class);
	
    public static void main( String[] args )
    {
        SpringApplication.run(PrintServiceStartup.class, args);
        logger.info("PrintServiceStartup success.");
    }
}
