package ivg.cn.monitor.redis;

public interface RedisChannelProcessor {

	void process(String channel, String message);
	
}
