package ivg.cn.tools.dislock;

public class DisLockResult {

	final String path;  // 锁节点
	
	String idPath;  // 资源锁节点
	
	boolean success = false;  // 获取成功
	
	public DisLockResult(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getIdPath() {
		return idPath;
	}

	public void setIdPath(String idPath) {
		this.idPath = idPath;
	}
	
}
