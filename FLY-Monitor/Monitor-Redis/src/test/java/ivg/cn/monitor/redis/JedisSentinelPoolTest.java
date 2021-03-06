package ivg.cn.monitor.redis;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.JedisSentinelPool;

public class JedisSentinelPoolTest {

	@Test
	public void testGetCurrentHostMaster() {
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxTotal(20);
		
		Set<String> sentinels = new LinkedHashSet<>();
		sentinels.add("192.168.5.131:10350");
		sentinels.add("192.168.5.131:10351");
		sentinels.add("192.168.5.131:10352");
		JedisSentinelPool sentinelPool = new JedisSentinelPool("ivgmaster", sentinels, poolConfig);
		HostAndPort hostAndPort = sentinelPool.getCurrentHostMaster();
		System.out.println(hostAndPort);
		
		Jedis jedis = sentinelPool.getResource();
		
		System.out.println(jedis.set("hello","nihaooo"));
		System.out.println(jedis.get("hello"));
		
		System.out.println(jedis.getClient().getHost()+":"+jedis.getClient().getPort());
		
		jedis.close();
		
		sentinelPool.close();
		
	}
	
	@Test
	public void testJedisPool() {
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxTotal(20);
		int soTimeout = 3000; // 命令执行时间 3s
		
		JedisPool jedisPool = new JedisPool(poolConfig, "192.168.5.131", 10311, soTimeout, false);
		Jedis jedis = jedisPool.getResource();
		try {
			jedis.subscribe(new JedisPubSub() {
				
				@Override
				public void onMessage(String channel, String message) {
					System.out.println("channel="+channel+", message="+message);
					super.onMessage(channel, message);
				}
				
			}, "+switch-master");
		} finally {
			jedis.close();
		}
		jedisPool.close();
	}
	
	@Test
	public void testInfo() {
		Jedis jedis = new Jedis("192.168.5.131", 10350);
		System.out.println(jedis.sentinelMasters());
//		jedis.sentinelMonitor("ivgmaster", "192.168.5.131", 10300, 2);
		
		
		jedis.subscribe(new JedisPubSub() {
			
			@Override
			public void onMessage(String channel, String message) {
				System.out.println(message);
			}
			
		}, "+switch-master");
		
		jedis.close();
	}
	
	@Test
	public void testSentinelMonitor() {
		Jedis jedis = new Jedis("192.168.5.131", 10354);
//		long begin = System.currentTimeMillis();
//		jedis.ping();
//		System.out.println("cost="+(System.currentTimeMillis()-begin));
		jedis.sentinelMonitor("ivgmaster", "192.168.5.131", 10300, 2);
		
		jedis.close();
	}
	
	
	
}
