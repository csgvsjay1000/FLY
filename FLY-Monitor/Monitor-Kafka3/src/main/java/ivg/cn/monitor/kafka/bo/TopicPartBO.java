package ivg.cn.monitor.kafka.bo;

import org.apache.kafka.common.TopicPartition;

import ivg.cn.common.ExpireToDelete;

public class TopicPartBO extends ExpireToDelete{

	private String topic;
	
	private int partition;
	
	int hash = -1;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getPartition() {
		return partition;
	}

	public void setPartition(int partition) {
		this.partition = partition;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (!(obj instanceof TopicPartBO)) {
			return false;
		}
		TopicPartBO targetObj = (TopicPartBO) obj;
		
		// 判断IP是否相等
		boolean topicEqual = false;
		if (this.topic == null) {
			if (targetObj.getTopic() == null) {
				topicEqual = true;
			}else {
				topicEqual = false;
			}
		}else {
			topicEqual = this.topic.equals(targetObj.getTopic());
		}
		
		// 判断 port是否相等
		boolean partEqual = this.partition == targetObj.getPartition();
		
		return topicEqual && partEqual ;
	}
	
    @Override
    public int hashCode() {
    	if (this.hash != -1) {
			return hash;
		}
    	int h = 17;
		h = h*31 +  (topic==null ? 0:topic.hashCode());
		h = h*31 + partition;
		this.hash = h;
		return h;
    }
    
    @Override
    public String toString() {
        return topic + "-" + partition;
    }
    
    public static TopicPartBO create(TopicPartition topicPartition) {
    	TopicPartBO topicPartBO = new TopicPartBO();
		topicPartBO.setTopic(topicPartition.topic());
		topicPartBO.setPartition(topicPartition.partition());
		return topicPartBO;
	}
}
