package ivg.cn.print.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;

import io.swagger.annotations.ApiOperation;
import ivg.cn.common.DreamResponse;
import ivg.cn.print.dto.PrintDetailDTO;
import ivg.cn.print.service.PrintService;

@RestController("/print")
public class PrintController {

	@Reference(timeout=6000,retries=0,actives=100,check=false)
	private PrintService printService;
	
	@ApiOperation("获取制单号")
	@PostMapping("/genId")
	public DreamResponse<String> genPrintId(){
		return printService.genPrintId();
	}
	
	@ApiOperation("提交制单需求")
	@PostMapping("/submit/print/bill")
	public DreamResponse<Integer> insertPrintDetail(@RequestBody PrintDetailDTO printDetailDTO){
		return printService.insertPrintDetail(printDetailDTO);
	}
	
}
