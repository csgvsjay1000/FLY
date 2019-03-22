package ivg.cn.common;


public class ExpireToDelete {
	private long updateTime;
	
	public ExpireToDelete() {
		this.updateTime = System.currentTimeMillis();
	}
	
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
