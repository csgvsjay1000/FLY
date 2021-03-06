package ivg.cn.monitor.redis;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class SentinelJedisCache {

	Map<String, JedisBean> jedisCache = new ConcurrentHashMap<>();
	
	public void subscribe(Jedis jedis,JedisPubSub jedisPubSub, String ...channels) {
		String addr = jedis.getClient().getHost()+":"+jedis.getClient().getPort();
		JedisBean jedisBean = jedisCache.get(addr);
		if (jedisBean != null) {
			return ;
		}
		synchronized (this) {
			jedisBean = jedisCache.get(addr);
			if (jedisBean != null) {
				return ;
			}
			jedisBean = new JedisBean(addr,jedis);
			RedisBean redisBean = new RedisBean(jedis.getClient().getHost(),jedis.getClient().getPort());
			new SubscribeThread(redisBean, jedisPubSub, channels).start();
			jedisCache.put(addr, jedisBean);
		}
		
	}
	
	public void cancelSubscribe(Jedis jedis) {
		
		
	}
	
	class SubscribeThread extends Thread{
		
		AtomicBoolean running = new AtomicBoolean(false);
		volatile Jedis jedis;
		JedisPubSub jedisPubSub;
		String[] channels;
		RedisBean redisBean;
		
		public SubscribeThread(RedisBean redisBean,JedisPubSub jedisPubSub, String[] channels) {
			super();
			this.jedisPubSub = jedisPubSub;
			this.channels = channels;
			this.redisBean = redisBean;
		}
		
		@Override
		public void run() {
			running.set(true);
			while (running.get()) {
				try {
					jedis = new Jedis(redisBean.getIp(), redisBean.getPort());
					jedis.subscribe(jedisPubSub, channels);
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					if (jedis != null) {
						jedis.close();
					}
				}
				
			}
		}
		
	}
	
	class JedisBean{
		String addr;  // ip:port
		
		Set<Jedis> jedisSet = new HashSet<>();

		public JedisBean(String addr, Jedis jedis) {
			super();
			this.addr = addr;
			jedisSet.add(jedis);
		}
		
	}
}
