package ivg.cn.monitor.kafka.service;

import java.util.concurrent.ConcurrentHashMap;

import ivg.cn.monitor.kafka.bo.Partitions;
import ivg.cn.monitor.kafka.bo.TopicInfo;
import ivg.cn.monitor.kafka.bo.TopicPartBO;
import ivg.cn.monitor.kafka.listener.TopicEventListener;

/**
 * 1、监听topic创建事件，监控系统启动时检查每个topic分区存活情况
 * */
public class TopicService implements TopicEventListener{

	BrokerService brokerService;
	
	ConcurrentHashMap<TopicPartBO, Integer/**leader*/> topicPartLeaderMap = new ConcurrentHashMap<>();
	
	@Override
	public void createEvent(TopicInfo topicInfo) {
		if (topicInfo == null || topicInfo.getPartitions() == null) {
			return;
		}
		for(Partitions part : topicInfo.getPartitions()){
			validateLeaderPartActive(part);
			validateRepls(part);
			upadteTopicPartLeader(topicInfo.getTopic(), part);
		}
		
	}

	@Override
	public void deleteEvent(String topic) {
		
		
	}

	@Override
	public void changeEvent(TopicInfo oldData, TopicInfo newData) {
		
		for(Partitions part : newData.getPartitions()){
			validateLeaderPartActive(part);
			validateRepls(part);
			upadteTopicPartLeader(newData.getTopic(), part);
		}
	}
	
	/**
	 * 验证topic副本数
	 * */
	private void validateRepls(Partitions part) {
		if (part.getIsr().size() < 2) {
			// TODO warn
		}
	}
	
	/**
	 * 验证分区存活
	 * */
	private void validateLeaderPartActive(Partitions part) {
		boolean isAlive = brokerService.isAlive(part.getLeader());
		if (isAlive) {
			// 如果存活，不操作
		}else {
			// 如果不存活发出警告
			
		}
	}
	
	/**
	 * 更新topic-part == leader 信息
	 * */
	private void upadteTopicPartLeader(String topic, Partitions part) {
		TopicPartBO partBO = new TopicPartBO();
		partBO.setTopic(topic);
		partBO.setPartition(part.getPart());
		
		Integer leader = topicPartLeaderMap.get(partBO);
		if (leader == null) {
			topicPartLeaderMap.put(partBO, part.getLeader());
		}else if (leader != part.getLeader()) {
			// leader change
			topicPartLeaderMap.put(partBO, part.getLeader());
		}
		
	}
	
	public void setBrokerService(BrokerService brokerService) {
		this.brokerService = brokerService;
	}
	
	public Integer leader(TopicPartBO partBO) {
		return topicPartLeaderMap.get(partBO);
	}

}
