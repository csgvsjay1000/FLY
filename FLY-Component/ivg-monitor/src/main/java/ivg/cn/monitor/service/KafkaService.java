package ivg.cn.monitor.service;


import ivg.cn.monitor.DreamResponse;
import ivg.cn.monitor.vo.ConsumerGroup;

public interface KafkaService {

	DreamResponse<String> listTopics();
	
	DreamResponse<ConsumerGroup> listAllConsumerGroups();
	
	DreamResponse<ConsumerGroup> listValidConsumerGroups();
	
}
