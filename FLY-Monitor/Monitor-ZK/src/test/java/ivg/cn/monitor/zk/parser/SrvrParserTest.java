package ivg.cn.monitor.zk.parser;

import org.junit.Test;

import ivg.cn.monitor.zk.ZKFourCmd;
import ivg.cn.monitor.zk.entity.ZkInstMonitor;
import ivg.cn.monitor.zk.nio.ZKConnection;
import ivg.cn.monitor.zk.nio.ZKReactor;
import ivg.cn.monitor.zk.nio.ZKSendCmdResult;


public class SrvrParserTest {

	@Test
	public void testSrvr() {
		
		ZKReactor reactor = new ZKReactor();
		reactor.start();
		
		ZKConnection conn = new ZKConnection(reactor,"192.168.5.131",10100);
		ZKSendCmdResult result = conn.sendCmd(ZKFourCmd.SRVR);
		
//		System.out.println(result.getResult());
//		ZkInstMonitor instMonitor = SrvrParser.parse(result.getResult());
//		System.out.println(instMonitor);
		
	}
	
	@Test
	public void testLantecy() {
		
		
	}
	
}
