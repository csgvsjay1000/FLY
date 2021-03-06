package ivg.cn.monitor.comp.zk;

import java.util.concurrent.CountDownLatch;

public class ZKSendCmdResult {

	String cmd;
	
	String result;
	
	CountDownLatch latch;
	
	public ZKSendCmdResult(String cmd) {
		this.cmd = cmd;
		latch = new CountDownLatch(1);
	}
	
	public void putResult(String result) {
		this.result = result;
		latch.countDown();
	}
	
	public String getResult() {
		return result;
	}
	
	public void waitResult() {
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
