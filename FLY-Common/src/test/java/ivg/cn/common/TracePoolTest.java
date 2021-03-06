package ivg.cn.common;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class TracePoolTest {

	@Test
	public void testPool() {
	
		ThreadPoolExecutor poolExecutor = new TraceThreadPoolExecutor("test",2, 2, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
		
		for (int i = 0; i < 5; i++) {
			poolExecutor.submit(new DivTask(i, i));
		}
		
	}
	
	class DivTask implements Runnable{
		
		int a;
		int b;
		
		public DivTask(int a, int b) {
			this.a = a;
			this.b = b;
		}
		
		@Override
		public void run() {
			double ret = a/b;
			System.out.println(ret);
		}
		
	}
	
}
