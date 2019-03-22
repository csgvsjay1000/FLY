package ivg.cn.monitor.kafka.bo;

import org.apache.kafka.common.Node;

import ivg.cn.common.ExpireToDelete;

public class ConsumerOffsetBO extends ExpireToDelete{

	private String clientId;
	
	private long offset;  // 目前消费的位置
	
	private long size;  // 目前提交位置
	
	private Node leader;  // leader节点
	
	private TopicPartBO topicPartBO;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	@Override
    public String toString() {
        return clientId+" --> "+ offset;
    }

	public TopicPartBO getTopicPartBO() {
		return topicPartBO;
	}

	public void setTopicPartBO(TopicPartBO topicPartBO) {
		this.topicPartBO = topicPartBO;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public Node getLeader() {
		return leader;
	}

	public void setLeader(Node leader) {
		this.leader = leader;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
	
}
