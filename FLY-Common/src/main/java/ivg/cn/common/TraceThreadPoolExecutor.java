package ivg.cn.common;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 可追踪堆栈、及任务执行状态的线程池, 
 * */
public class TraceThreadPoolExecutor extends ThreadPoolExecutor{

	public TraceThreadPoolExecutor(String name,int corePoolSize,
            int maximumPoolSize,
            long keepAliveTime,
            TimeUnit unit,
            BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, 0, TimeUnit.SECONDS, workQueue,new ThreadFactory() {
			AtomicInteger index = new AtomicInteger(0);
			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r, name+"-"+index.getAndIncrement());
				return thread;
			}
		});
	}
	
	@Override
	public void execute(Runnable command) {
		super.execute(this.wrap(command, Thread.currentThread().getName()));
	}
	
	@Override
	public Future<?> submit(Runnable task) {
		return super.submit(this.wrap(task, Thread.currentThread().getName()));
	}
	
	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		super.beforeExecute(t, r);
	}
	
	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		super.afterExecute(r, t);
	}
	
	@Override
	protected void terminated() {
		super.terminated();
	}
	
	private Runnable wrap(Runnable task, String clientThreadName) {
		return new Runnable() {
			
			@Override
			public void run() {
				try {
					task.run();
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
				
			}
		};
	}

}
