package ivg.cn.monitor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket testApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("ivg.cn.monitor.controller")).build()
				.apiInfo(testApiInfo());
	}
	
	private ApiInfo testApiInfo() {
		return new ApiInfoBuilder()
	            .title("监控平台 API V2.1.0")//大标题
	            .version("V2.1.0")//版本
	            .termsOfServiceUrl("NO terms of service")
	            .build();
	}
	
}
