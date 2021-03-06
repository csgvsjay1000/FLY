package ivg.cn.redic;

import java.util.Random;

import redis.clients.jedis.Jedis;

/**
 * 重写Jedis方法
 * */
public class Redic extends AbstractRedic{

	Random random = new Random(10);
	
	@Override
	public String set(String key, String value) {
		Jedis jedis = getWriteJedis(key);
		try {
			return jedis.set(key, value);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		} 
	}
	
	public String setRandomEx(String key, String value, int seconds) {
		Jedis jedis = getWriteJedis(key);
		try {
			return jedis.setex(key, seconds+random.nextInt(1),value);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		} 
	}
	
	@Override
	public String get(String key) {
		Jedis jedis = getWriteJedis(key);
		try {
			return jedis.get(key);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		} 
	}
	
}
