package ivg.cn.print.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ivg.cn.print.service.config.bean.VestaIdBean;
import ivg.cn.vesta.IdService;
import ivg.cn.vesta.impl.IdServiceImpl;

@Configuration
public class VestaIdConfig {

	@Autowired
	VestaIdBean vestaIdBean;
	
	@Bean
	public IdService idService() {
		IdServiceImpl idService = new IdServiceImpl();
		idService.setMachineId(vestaIdBean.getMachineId());
		return idService;
	}
	
}
