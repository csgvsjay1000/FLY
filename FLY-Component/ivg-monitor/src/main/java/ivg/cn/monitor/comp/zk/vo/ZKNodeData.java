package ivg.cn.monitor.comp.zk.vo;


/**
 * zookeeper 单个节点的数据
 * */
public class ZKNodeData {

	private String ctime;  // 节点创建时间
	
	private String mtime;  // 节点更新时间
	
	private int version;  // 当前节点的版本
	
	private int cversion;  // 子节点的版本
	
	private long ephemeralOwner;  // 节点的所有者，若是临时节点，则是连接客户端的会话ID
	
    private int dataLength;  // 数据长度
    
    private int numChildren;  // 子节点数
	
	private String dataStr;
	
	private byte[] data;

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getDataStr() {
		return dataStr;
	}

	public void setDataStr(String dataStr) {
		this.dataStr = dataStr;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getMtime() {
		return mtime;
	}

	public void setMtime(String mtime) {
		this.mtime = mtime;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getCversion() {
		return cversion;
	}

	public void setCversion(int cversion) {
		this.cversion = cversion;
	}

	public long getEphemeralOwner() {
		return ephemeralOwner;
	}

	public void setEphemeralOwner(long ephemeralOwner) {
		this.ephemeralOwner = ephemeralOwner;
	}

	public int getDataLength() {
		return dataLength;
	}

	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}

	public int getNumChildren() {
		return numChildren;
	}

	public void setNumChildren(int numChildren) {
		this.numChildren = numChildren;
	}
	
}
