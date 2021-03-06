package ivg.cn.redic.strategy;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 轮询策略
 * */
public class RoundRobinSelectStrategy implements SelectStrategy{

	private AtomicLong iter = new AtomicLong(0);
	
	@Override
	public int select(int count) {
		long iterIndex = iter.incrementAndGet();
		if (iterIndex == Long.MAX_VALUE) {
			iter.set(0);
		}
		return (int) (iterIndex%count);
	}

}
