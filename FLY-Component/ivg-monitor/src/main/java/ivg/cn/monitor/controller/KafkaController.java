package ivg.cn.monitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import ivg.cn.monitor.DreamResponse;
import ivg.cn.monitor.service.KafkaService;
import ivg.cn.monitor.vo.ConsumerGroup;

@RestController("/kafka")
public class KafkaController {

	@Autowired
	private KafkaService kafkaService;
	
	@ApiOperation("获取topic列表")
	@PostMapping("/topics")
	public DreamResponse<String> listTopics() {
		return kafkaService.listTopics();
	}
	
	@ApiOperation("获取所有consumer列表")
	@PostMapping("/allConsumerGroups")
	public DreamResponse<ConsumerGroup> listAllConsumerGroups(){
		return kafkaService.listAllConsumerGroups();
	}
	
	@ApiOperation("获取有效的consumer列表")
	@PostMapping("/validConsumerGroups")
	public DreamResponse<ConsumerGroup> listValidConsumerGroups(){
		return kafkaService.listValidConsumerGroups();
	}
	
	
	
}
