package ivg.cn.tools.dislock;

public interface DisLock {

	void init(String zkurl, String lookName);
	
	DisLockResult tryLock(String resources, long timeoutMills);
	
	void release(String resource);
	
	void release(DisLockResult resource, boolean deleteIdPath);
	
}
