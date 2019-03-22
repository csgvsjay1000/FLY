package ivg.cn.monitor.kafka.listener;

import ivg.cn.monitor.kafka.bo.TopicInfo;

public interface TopicEventListener extends ZKListener{

	void createEvent(TopicInfo topicInfo);
	
	void deleteEvent(String topic);
	
	void changeEvent(TopicInfo oldData,TopicInfo newData);
}
