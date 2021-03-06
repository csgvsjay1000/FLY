package ivg.cn.common;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorUtils {

	/**
	 * 创建一个固定线程数、异常可追踪的线程池
	 * */
	public static ThreadPoolExecutor traceFixedThreadPool(String name, int threadNums) {
		return new TraceThreadPoolExecutor(name, threadNums, threadNums, 0, TimeUnit.MILLISECONDS, 
				new LinkedBlockingQueue<Runnable>());
	}
	
	/**
	 * 创建单线程池
	 * */
	public static ScheduledExecutorService namedSingleScheduleExecutor(String name) {
		return Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
			
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, name);
			}
		});
		
		
		
		
	}
	
}
