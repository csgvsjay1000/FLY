package ivg.cn.monitor.kafka.bo;

import java.util.List;

public class TopicInfo {

	String topic;
	
	List<Partitions> partitions;
	
	String version;

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public List<Partitions> getPartitions() {
		return partitions;
	}

	public void setPartitions(List<Partitions> partitions) {
		this.partitions = partitions;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	@Override
    public String toString() {
       StringBuilder sb = new StringBuilder();
       for (Partitions partitions2 : partitions) {
    	   sb.append("part:").append(partitions2.getPart());
    	   sb.append("isr:").append(partitions2.getIsr());
       }
       
       return sb.toString();
    }

	
}
