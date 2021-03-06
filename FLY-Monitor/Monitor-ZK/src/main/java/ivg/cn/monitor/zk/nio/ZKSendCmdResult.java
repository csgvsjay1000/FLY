package ivg.cn.monitor.zk.nio;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ZKSendCmdResult {

	String cmd;
	
	String result;
	
	CountDownLatch latch;
	
	boolean success;  // 请求成功
	
	public ZKSendCmdResult(String cmd) {
		this.cmd = cmd;
		latch = new CountDownLatch(1);
	}
	
	public void putResult(String result) {
		this.result = result;
		success = true;
		latch.countDown();
	}
	
	public String getResult() {
		return result;
	}
	
	public void waitResult(long millisTimeout) {
		try {
			latch.await(millisTimeout,TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
}
