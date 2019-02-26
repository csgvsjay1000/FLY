package ivg.cn.vesta.impl.timer;

import java.util.Date;

import ivg.cn.vesta.impl.bean.IdMeta;
import ivg.cn.vesta.impl.bean.IdType;


public interface Timer {

    long EPOCH = 1514736000000L;  // 1970年开始的时间戳
	
	void init(IdMeta idMeta, IdType idType);
	
	long genTime();
	
	/** 时间戳转为时间类型 */
	Date transTime(long time);
	
	void validateTimestamp(long lastTimestamp, long timestamp);
	
	long tillNextTimeUnit(long lastTimestamp);
	
}
