package ivg.cn.monitor.redis;

import java.math.BigDecimal;
import java.util.Random;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Assert;
import org.junit.Test;

import ivg.cn.monitor.redis.util.ConcurrentTestTools;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisTest {

	
	@Test
	public void testInfo() {
		Jedis jedis = new Jedis("192.168.5.131", 10301);
//		System.out.println(jedis.set("hello","nihao"));
//		System.out.println(jedis.sentinelMasters());
		
		System.out.println(jedis.info());
		System.out.println("----------------------------------------------------------------------------");
//		System.out.println(jedis.sentinelMasters());
//		System.out.println(jedis.get("hello"));
		
//		jedis.del("hello");
		
		jedis.close();
	}
	
	@Test
	public void testPool() {
		
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxTotal(20);
		int soTimeout = 3000; // 命令执行时间 3s
		
		JedisPool jedisPool = new JedisPool(poolConfig, "192.168.5.131", 10300, soTimeout, false);
		
		String OK = "OK";
		
		Random random = new Random(100000000);
		
		ConcurrentTestTools testTools = new ConcurrentTestTools(100, new Runnable() {
			
			@Override
			public void run() {
				for (int i = 10; i < 10000; i++) {
					Jedis jedis = null;
					try {
						jedis = jedisPool.getResource();
						String ret = jedis.set("key"+random.nextInt(), "value"+i);
						Assert.assertEquals(OK, ret);
						Thread.sleep(1);
					} catch (Exception e) {
						e.printStackTrace();
					}finally {
						if (jedis != null) {
							jedis.close();
						}
					}
				}
			}
		});
		
		testTools.start();
		
		jedisPool.close();
	}
	
	@Test
	public void testRedisBean() {
		
		long begin = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			RedisBean redisBean = new RedisBean();
			redisBean.setPort(1001+i);
			redisBean.setIp("192.168.5.13"+i);
			
			RedisBean redisBean1 = new RedisBean();
			redisBean1.setPort(1001+i);
			redisBean1.setIp("192.168.5.13"+i);
			Assert.assertEquals(true, redisBean.equals(redisBean1));
		}
		
		System.out.println(String.format("cost %d(ms)", System.currentTimeMillis()-begin));
		
	}
	
	@Test
	public void testConfig() {
		Jedis jedis = new Jedis("192.168.5.131", 10301);
		System.out.println(jedis.configGet("slowlog-log-slower-than"));
		System.out.println(jedis.configGet("slowlog-max-len"));
		
		 System.out.println(jedis.slowlogGet());
		 System.out.println(jedis.slowlogLen());
		
		jedis.close();
	}
	
	@Test
	public void testShutdown() {
		Jedis jedis = new Jedis("192.168.5.131", 10300);
		System.out.println(jedis.shutdown());
		
		jedis.close();
	}
	
	@Test
	public void testSlaveof() {
		Jedis jedis = new Jedis("192.168.5.131", 10302);
		System.out.println(jedis.slaveof("192.168.5.131", 10300));
		
		jedis.close();
	}
	
	
	@Test
	public void testWeiyi() {
		
		
	}
	
}
