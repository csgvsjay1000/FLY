package ivg.cn.print.service.timer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;

import ivg.cn.print.dao.PrintDao;
import ivg.cn.print.dao.PrintDetailDao;
import ivg.cn.print.service.iner.InerPrintService;

@Component
public class ScanPrintEpcTimer {
	private static Logger log = LoggerFactory.getLogger(ScanPrintEpcTimer.class);

	ScheduledExecutorService scheduledExecutorService;
	
	@Reference
	private PrintDao printDao;
	
	@Reference
	private PrintDetailDao printDetailDao;
	
	@Autowired
	private InerPrintService inerPrintService;
	
	public ScanPrintEpcTimer() {
		scheduledExecutorService = Executors.newScheduledThreadPool(1, new ThreadFactory() {
			
			AtomicInteger index = new AtomicInteger(0);
			
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "ScanPrintEpcTimer-"+index.getAndIncrement());
			}
		});
		scheduledExecutorService.scheduleAtFixedRate(task(), 1, 5, TimeUnit.SECONDS);  // 每隔5分钟扫描一次
	}
	
	public Runnable task() {
		
		return new Runnable() {
			
			@Override
			public void run() {
				executeTask();
			}
		};
	}
	
	public void executeTask() {
		// 1、扫描最近半小时内未完成的打印单
		// 2、调用生成EPC服务,并把制单任务改成制单中
		try {
			inerPrintService.scanNoPrintBill();
		} catch (Exception e) {
			log.error("receive message error.",e);
		}
		
	}
	
}
