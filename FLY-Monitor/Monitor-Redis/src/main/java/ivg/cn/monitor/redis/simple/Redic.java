package ivg.cn.monitor.redis.simple;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.JedisPool;

public class Redic {

	JedisPool jedisPool = new JedisPool(new GenericObjectPoolConfig(), "", 10300);
	
	public void set() {
		
	}
	
	
}
