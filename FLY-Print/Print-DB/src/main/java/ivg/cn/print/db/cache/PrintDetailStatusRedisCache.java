package ivg.cn.print.db.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ivg.cn.redic.Redic;

@Component
public class PrintDetailStatusRedisCache {

	private final int ExpireTime = 60*10;  // 10 分钟
	
	@Autowired
	private Redic redic;
	
	public void putFinshed(String key) {
		redic.setRandomEx(key, "OK", ExpireTime);
	}
	
	public boolean hasFinshed(String key) {
		String val = redic.get(key);
		if ("OK".equals(val)) {
			return true;
		}
		return false;
	}
	
}
