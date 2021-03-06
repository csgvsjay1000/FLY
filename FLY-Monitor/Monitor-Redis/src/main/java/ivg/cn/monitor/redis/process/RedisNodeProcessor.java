package ivg.cn.monitor.redis.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ivg.cn.monitor.redis.RedisChannelProcessor;

/**
 * chennel=+sdown, message=slave 192.168.5.131:10302 192.168.5.131 10302 @ ivgmaster 192.168.5.131 10300
 * -sdown
 * */
public class RedisNodeProcessor implements RedisChannelProcessor{

	Logger log = LoggerFactory.getLogger(RedisNodeProcessor.class);

	@Override
	public void process(String chennel, String message) {
		log.info("chennel={}, message={}", chennel, message);
		
	}

}
