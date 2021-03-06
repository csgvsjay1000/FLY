package ivg.cn.replenish.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;

import ivg.cn.common.DreamResponse;
import ivg.cn.print.entity.PrintDetailEpc;
import ivg.cn.replenish.entity.ValidateEpcDTO;
import ivg.cn.replenish.service.ReplenishService;

@RestController
@RequestMapping("/replenish")
public class ReplenishController {

	@Reference(version="1",timeout=3000)
	private ReplenishService replenishService;
	
	/**
	 * 前端生成补货单时，校验EPC接口
	 * */
	@PostMapping("/epc")
	public DreamResponse<PrintDetailEpc> select(@RequestBody ValidateEpcDTO epc){
		return replenishService.select(epc);
	}
}
