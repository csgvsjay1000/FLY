package ivg.cn.monitor.redis;

public class RedisBean {

	private String ip;
	
	private int port;
	
	public RedisBean() {
		
	}

	public RedisBean(String ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (!(obj instanceof RedisBean)) {
			return false;
		}
		RedisBean targetObj = (RedisBean) obj;
		
		// 判断IP是否相等
		boolean ipEqual = false;
		if (this.ip == null) {
			if (targetObj.getIp() == null) {
				ipEqual = true;
			}else {
				ipEqual = false;
			}
		}else {
			ipEqual = this.ip.equals(targetObj.getIp());
		}
		
		// 判断 port是否相等
		boolean portEqual = this.port == targetObj.getPort();
		
		return ipEqual && portEqual;
	}
	
	@Override
	public int hashCode() {
		int h = 17;
		h = h*31 + ip.hashCode();
		h = h*31 + port;
		return h;
	}
	
}
