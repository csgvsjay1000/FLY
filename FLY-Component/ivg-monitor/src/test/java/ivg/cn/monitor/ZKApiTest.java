package ivg.cn.monitor;

import org.junit.Test;

import ivg.cn.monitor.comp.zk.ZKApi;

public class ZKApiTest {

	@Test
	public void testZk() {
		
		ZKApi zkApi = new ZKApi();
		zkApi.openConn("39.108.11.154:2181");
		
		zkApi.listChildren("/");
	}
	
	@Test
	public void testRecusionDel() {
		
		ZKApi zkApi = new ZKApi();
		zkApi.openConn("39.108.11.154:2181");
		zkApi.recursionDelete("/fly/printLock");

	}
	
}
