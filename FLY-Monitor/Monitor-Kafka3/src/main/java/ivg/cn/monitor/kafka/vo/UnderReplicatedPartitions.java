package ivg.cn.monitor.kafka.vo;

/**
 * 备份不足分区数
 * */
public class UnderReplicatedPartitions {

	private int brokerId;
	
	int underReplicatedPartitions;

	public int getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(int brokerId) {
		this.brokerId = brokerId;
	}

	public int getUnderReplicatedPartitions() {
		return underReplicatedPartitions;
	}

	public void setUnderReplicatedPartitions(int underReplicatedPartitions) {
		this.underReplicatedPartitions = underReplicatedPartitions;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("brokerId=").append(brokerId).append(", ");
		sb.append("underReplicatedPartitions=").append(underReplicatedPartitions).append(" ");
		return sb.toString();
	}
}
