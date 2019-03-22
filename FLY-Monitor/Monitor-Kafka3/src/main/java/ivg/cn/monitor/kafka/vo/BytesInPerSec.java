package ivg.cn.monitor.kafka.vo;

import java.math.BigDecimal;

/**
 * 消息入网速率
 * */
public class BytesInPerSec {

	private int brokerId;
	
	BigDecimal meanRate;  // 每秒平均字节数
	
	BigDecimal oneMinuteRate;  // 过去1分钟平均速率
	
	BigDecimal fifteenMinuteRate;  // 过去15分钟平均速率

	public int getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(int brokerId) {
		this.brokerId = brokerId;
	}

	public BigDecimal getMeanRate() {
		return meanRate;
	}

	public void setMeanRate(BigDecimal meanRate) {
		this.meanRate = meanRate;
	}

	public BigDecimal getOneMinuteRate() {
		return oneMinuteRate;
	}

	public void setOneMinuteRate(BigDecimal oneMinuteRate) {
		this.oneMinuteRate = oneMinuteRate;
	}

	public BigDecimal getFifteenMinuteRate() {
		return fifteenMinuteRate;
	}

	public void setFifteenMinuteRate(BigDecimal fifteenMinuteRate) {
		this.fifteenMinuteRate = fifteenMinuteRate;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("brokerId=").append(brokerId).append(", ");
		sb.append("meanRate=").append(meanRate).append(", ");
		sb.append("oneMinuteRate=").append(oneMinuteRate).append(", ");
		sb.append("fifteenMinuteRate=").append(fifteenMinuteRate).append(" ");
		return sb.toString();
	}
}
