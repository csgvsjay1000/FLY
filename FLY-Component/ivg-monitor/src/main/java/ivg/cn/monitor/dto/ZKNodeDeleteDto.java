package ivg.cn.monitor.dto;

public class ZKNodeDeleteDto {

	private String path;
	
	private int version;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
}
