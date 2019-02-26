package ivg.cn.monitor;

import org.junit.Test;

import ivg.cn.monitor.comp.zk.ZKConnection;
import ivg.cn.monitor.comp.zk.ZKReactor;
import ivg.cn.monitor.comp.zk.ZKSendCmdResult;

public class ZKConnectionTest {

	@Test
	public void testZk() {
		
		
	}
	
	public static void main(String[] args) {
		ZKReactor reactor = new ZKReactor();
		reactor.start();
		
		ZKConnection conn = new ZKConnection(reactor);
		ZKSendCmdResult result = conn.sendCmd("stat");
		System.out.println(result.getResult());
	}
	
}
