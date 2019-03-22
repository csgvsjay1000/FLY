package ivg.cn.monitor.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.kafka.clients.admin.ConsumerGroupListing;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ivg.cn.kclient.KClientAdmin;
import ivg.cn.monitor.DreamResponse;
import ivg.cn.monitor.service.KafkaService;
import ivg.cn.monitor.vo.ConsumerGroup;

@Service
public class KafkaServiceImpl implements KafkaService{

//	@Autowired
	private KClientAdmin clientAdmin;
	
	public DreamResponse<String> listTopics() {
		DreamResponse<String> response = DreamResponse.createOKResponse();
		response.setData(clientAdmin.listTopics());
		return response;
	}
	
	public DreamResponse<ConsumerGroup> listAllConsumerGroups() {
		DreamResponse<ConsumerGroup> response = DreamResponse.createOKResponse();
		
		Collection<ConsumerGroupListing> result = clientAdmin.listAllConsumerGroups();
		List<ConsumerGroup> rList = new ArrayList<>(result.size());
		for (ConsumerGroupListing c : result) {
			ConsumerGroup cGroup = new ConsumerGroup();
			cGroup.setGroupId(c.groupId());
			cGroup.setSimpleConsumerGroup(c.isSimpleConsumerGroup());
			rList.add(cGroup);
		}
		response.setData(rList);
		return response;
	}
	
	public DreamResponse<ConsumerGroup> listValidConsumerGroups() {
		DreamResponse<ConsumerGroup> response = DreamResponse.createOKResponse();
		
		Collection<ConsumerGroupListing> result = clientAdmin.listValidConsumerGroups();
		List<ConsumerGroup> rList = new ArrayList<>(result.size());
		for (ConsumerGroupListing c : result) {
			ConsumerGroup cGroup = new ConsumerGroup();
			cGroup.setGroupId(c.groupId());
			cGroup.setSimpleConsumerGroup(c.isSimpleConsumerGroup());
			rList.add(cGroup);
		}
		response.setData(rList);
		return response;
	}
	
}
