package ivg.cn.print.service.impl;

import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.stereotype.Component;

import ivg.cn.common.ExecutorUtils;

@Component
public class PrintTaskBussinessPool {

	ThreadPoolExecutor businessPool = null;
	
	public PrintTaskBussinessPool() {
		businessPool = ExecutorUtils.traceFixedThreadPool("print-pool", 10);
	}
	
	public Future<?> submitTask(Runnable task) {
		return businessPool.submit(task);
	}
	
	public void execute(Runnable task) {
		businessPool.execute(task);
	}
}
