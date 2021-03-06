package ivg.cn.print.db.cache;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

public class PrintDetailStatusCache {

	ConcurrentHashMap<String, CacheNode> cMap = new ConcurrentHashMap<String, CacheNode>();
	
	static long CacheTimeout = 1000*60*5;  // 5分钟
	
	static PrintDetailStatusCache INSTANCE = new PrintDetailStatusCache();
	public static PrintDetailStatusCache getInstance() {
		return INSTANCE;
	}
	
	ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1, new ThreadFactory() {
		
		@Override
		public Thread newThread(Runnable r) {
			
			return new Thread(r, "print-redis-clear");
		}
	});
	
	public PrintDetailStatusCache() {
		start();
	}
	
	public void start() {
		Iterator<Map.Entry<String, CacheNode>> it = cMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, CacheNode> value = it.next();
			if (value.getValue().timeout()) {
				it.remove();
			}
		}
	}
	
	public void putFinshed(String key) {
		if (!cMap.contains(key)) {
			CacheNode node = new CacheNode();
			node.setFinshed(true);
			node.setLastAtime(System.currentTimeMillis());
			cMap.put(key, node);
		}
	}
	
	public boolean hasFinshed(String key) {
		CacheNode cacheNode = cMap.get(key);
		if (cacheNode != null) {
			cacheNode.setLastAtime(System.currentTimeMillis());
			return true;
		}
		return false;
	}
	
	public static class CacheNode{
		
		long lastAtime;  // 最后访问时间
		
		boolean finshed;

		public long getLastAtime() {
			return lastAtime;
		}

		public void setLastAtime(long lastAtime) {
			this.lastAtime = lastAtime;
		}

		public boolean isFinshed() {
			return finshed;
		}

		public void setFinshed(boolean finshed) {
			this.finshed = finshed;
		}
		
		public boolean timeout() {
			long ecs = System.currentTimeMillis() - lastAtime;
			if (ecs > CacheTimeout) {
				// 超过5分钟没有访问则，认为已处理完
				return true;
			}
			return false;
		}
		
	}
	
}
