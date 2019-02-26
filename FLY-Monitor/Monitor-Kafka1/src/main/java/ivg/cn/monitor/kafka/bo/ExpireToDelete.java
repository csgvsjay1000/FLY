package ivg.cn.monitor.kafka.bo;


public class ExpireToDelete {
	private long updateTime;
	
	public ExpireToDelete update() {
		this.updateTime = System.currentTimeMillis();
		return this;
	}
	
	public boolean expire(long expiredTime) {
		if (System.currentTimeMillis() - updateTime > expiredTime) {
			return true;
		}
		return false;
	}
}
