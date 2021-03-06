package ivg.cn.monitor.redis;

import java.util.HashMap;
import java.util.Map;

public class GlobalCache {

	Map<RedisBean, TotalCommand<String, Integer>> totalCommandsProcessedMap = new HashMap<>();
	Map<RedisBean, TotalCommand<String, Float>> cpuUsedTimeMap = new HashMap<>();
	
	public static long TimeIntervel = 10000;  // 每隔10s采集一次
	
	private static GlobalCache instance = new GlobalCache();
	
	public static GlobalCache getInstance() {
		return instance;
	}
	
	private GlobalCache() {
		
	}
	
	public String calculateQPS(RedisBean redisBean, int nowTotalCommand) {
		TotalCommand<String, Integer> old = totalCommandsProcessedMap.get(redisBean);
		if (old == null) {
			synchronized (instance) {
				if (old == null) {
					old = new TotalCommand<String, Integer>();
					totalCommandsProcessedMap.put(redisBean, old);
					
					old.setcInterface(new CalculateInterface<String, Integer>() {

						@Override
						public String getResult(Integer nowValue, Integer preValue,long cost) {
							
							if (preValue == null) {
								return "0";
							}else {
								int commands = nowValue - preValue;
								
								float qps = commands/(((float)cost)/1000f);
								return String.valueOf(qps);
							}
						}
					});
				}
			}
		}
		return old.getResult(nowTotalCommand);
	}
	
	
	/**  */
	public String calculateCPU(RedisBean redisBean, Float nowTotalCommand) {
		TotalCommand<String, Float> old = cpuUsedTimeMap.get(redisBean);
		if (old == null) {
			synchronized (instance) {
				if (old == null) {
					old = new TotalCommand<String, Float>();
					cpuUsedTimeMap.put(redisBean, old);
					
					old.setcInterface(new CalculateInterface<String, Float>() {

						@Override
						public String getResult(Float nowValue, Float preValue,long cost) {
							
							if (preValue == null) {
								return "0";
							}else {
								Float commands = nowValue - preValue;
								
								float qps = (commands/(float)(cost/1000))*100;
								return String.valueOf(qps);
							}
						}
					});
				}
			}
		}
		return old.getResult(nowTotalCommand);
	}
	
	
	static class TotalCommand<T,K>{
		volatile K preTotalCommand;
		
		volatile long millisTime;  // ms
		
		CalculateInterface<T,K> cInterface;

		public K getPreTotalCommand() {
			return preTotalCommand;
		}

		public void setPreTotalCommand(K preTotalCommand) {
			this.preTotalCommand = preTotalCommand;
		}

		public long getMillisTime() {
			return millisTime;
		}

		public void setMillisTime(long millisTime) {
			this.millisTime = millisTime;
		}
		
		public T getResult(K nowValue) {
			if (cInterface != null) {
				long currentTime = System.currentTimeMillis();
				T ret = cInterface.getResult(nowValue, preTotalCommand, currentTime - millisTime);
				preTotalCommand = nowValue;
				millisTime = currentTime;
				return ret;
			}
			return null;
		}
		
		public void setcInterface(CalculateInterface<T,K> cInterface) {
			this.cInterface = cInterface;
		}
		
		public CalculateInterface<T,K> getcInterface() {
			return cInterface;
		}
	}
	
	interface CalculateInterface<T,K>{
		T getResult(K nowValue, K preValue, long beforTime);
	}
}
