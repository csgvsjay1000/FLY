package ivg.cn.monitor.redis.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ivg.cn.monitor.redis.RedisChannelProcessor;

public class SentinelProcessor implements RedisChannelProcessor{

	Logger log = LoggerFactory.getLogger(SentinelProcessor.class);

	@Override
	public void process(String chennel, String message) {
		log.info("chennel={}, message={}", chennel, message);
		
	}

}
