package ivg.cn.redic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedicTest {

	
	@Test
	public void testRedic() {
		
		Redic redic = new Redic();
		List<String> nodes = new ArrayList<String>();
		nodes.add("192.168.5.131:10300");
		redic.setNodeConnStrs(nodes);
		redic.init();
		
//		String key = "hello";
//		redic.set("hello", "test");
//		System.out.println(redic.get(key));
		
		
		String info = redic.info();
		System.out.println(info);
		
		redic.close();
		
	}
	
	@Test
	public void testShareRedis() {
		
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		List<JedisShardInfo> shardInfos = new ArrayList<>();
		
		JedisShardInfo shardInfo1 = new JedisShardInfo("192.168.5.131", 10300);
		JedisShardInfo shardInfo2 = new JedisShardInfo("192.168.5.131", 10301);
		JedisShardInfo shardInfo3 = new JedisShardInfo("192.168.5.131", 10302);
		shardInfos.add(shardInfo1);
		shardInfos.add(shardInfo2);
		shardInfos.add(shardInfo3);
		
		ShardedJedisPool jedisPool = new ShardedJedisPool(poolConfig, shardInfos);
		
		ShardedJedis jedis = jedisPool.getResource();
		
		for (int i = 0; i < 1000; i++) {
			jedis.set("string-key-"+i,"1000"+i);
		}
		jedis.close();
		jedisPool.close();
		
	}
	
}
