package ivg.cn.print;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;

@SpringBootApplication
@DubboComponentScan(basePackages = "ivg.cn.print.controller")
public class PrintControllerStartup {

	public static void main(String[] args) {
		SpringApplication.run(PrintControllerStartup.class, args);
	}
	
}
