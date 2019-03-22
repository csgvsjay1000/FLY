package ivg.cn.monitor.kafka.bo;

import java.util.List;

public class Partitions {

	private int part;
	
	private int leader;
	
	private List<Integer> isr;

	public int getPart() {
		return part;
	}

	public void setPart(int part) {
		this.part = part;
	}

	public int getLeader() {
		return leader;
	}

	public void setLeader(int leader) {
		this.leader = leader;
	}

	public List<Integer> getIsr() {
		return isr;
	}

	public void setIsr(List<Integer> isr) {
		this.isr = isr;
	}
	
}
