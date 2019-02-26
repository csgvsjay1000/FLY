package ivg.cn.vesta.impl.populater;

import ivg.cn.vesta.Id;
import ivg.cn.vesta.impl.bean.IdMeta;
import ivg.cn.vesta.impl.timer.Timer;

public abstract class BasePopulator implements IdPopulator{

	protected long sequence = 0;
	protected long lastTimestamp = 0;
	
//	@Override
	public void doPopulateId(Timer timer, Id id, IdMeta idMeta) {
		long timestamp = timer.genTime();
		timer.validateTimestamp(lastTimestamp, timestamp);
		
		if (timestamp == lastTimestamp) {
			// 同一秒类或者毫秒类
			sequence++;
			sequence &= idMeta.getSeqBitsMask(); // 这个操作的意思？
			if (sequence == 0) {
				// sequence 超出了位数 
				timestamp = timer.tillNextTimeUnit(lastTimestamp);
			}
		}else {
			lastTimestamp = timestamp;
			sequence = 0;
		}
		id.setSeq(sequence);
		id.setTime(timestamp);
	}
}
