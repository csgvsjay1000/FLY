package ivg.cn.tools;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Before;
import org.junit.Test;

import ivg.cn.tools.dislock.DisLock;
import ivg.cn.tools.dislock.DisLockResult;
import ivg.cn.tools.dislock.ZkDisLock;

public class DisLockTest {

	ZkDisLock disLock;
	
	@Before
	public void init() {
		disLock = new ZkDisLock();
		disLock.init("39.108.11.154:2181", "printLock");
	}
	
//	@Test
	public void testZK() {
		
		DisLockResult releaseLock = null;
		try {
			releaseLock = disLock.tryLock("1234567", 0);
			if (releaseLock.isSuccess()) {
				System.out.println(Thread.currentThread()+" obtain a lock ...");
				System.out.println(Thread.currentThread()+" doSomeWork...");
				Thread.sleep(5000);
				System.out.println(Thread.currentThread()+" finshed...");
			}else {
				System.out.println(Thread.currentThread()+" not obtain a lock ...");
			}
		}
		catch (Exception e) {
			
		}finally {
			if (releaseLock != null) {
				disLock.release(releaseLock.getPath());
				System.out.println(Thread.currentThread()+" release lock...");
			}
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		
		AtomicInteger obtainTimes = new AtomicInteger(0);
		AtomicInteger noBbtainTimes = new AtomicInteger(0);
		int loopTime = 1000;
		int thread = 50;
		int totalTest = loopTime*thread;
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				DisLock disLock = new ZkDisLock();
				disLock.init("39.108.11.154:2181", "printLock");
				for (int i = 0; i < loopTime; i++) {
					DisLockResult releaseLock = null;
					try {
						releaseLock = disLock.tryLock("1234567"+i, 0);
						if (releaseLock.isSuccess()) {
							obtainTimes.incrementAndGet();
						}else {
							noBbtainTimes.incrementAndGet();
						}
					}
					catch (Exception e) {
						
					}finally {
						if (releaseLock != null) {
							disLock.release(releaseLock.getPath());
						}
					}
				}
			}
		};
		
		ConcurrentTestTools testTools = new ConcurrentTestTools(thread, runnable);
		long begin = System.currentTimeMillis();
		testTools.start();
		long cost = System.currentTimeMillis() - begin;
		float tps = ((float)obtainTimes.get())/((float)cost/1000);
		System.out.println(String.format("total test times %d, cost %d(ms)", totalTest,cost));
		System.out.println(String.format("success access lock times %d, failed: %d", obtainTimes.get(),noBbtainTimes.get()));
		System.out.println(String.format("thread nums: %d, TPS: %.2f", thread,tps));
	}
	
	@Test
	public void testPrint() {
		float tps = ((float)1000)/((float)43586/1000);
		System.out.println(String.format("thread nums: %d, TPS: %.2f", 1,tps));

	}
}
