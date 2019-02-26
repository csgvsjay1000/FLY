package ivg.cn.dbsplit;

import org.junit.Test;

public class LogConfigTest {

	@Test
	public void testLog() {
		
		LogConfig.getInstance().makeDebug();
		
		System.out.println("isEnableDebug: "+LogConfig.getInstance().isEnableDebug());
		System.out.println("isEnableInfo: "+LogConfig.getInstance().isEnableInfo());
		System.out.println("isEnableWarn: "+LogConfig.getInstance().isEnableWarn());
		System.out.println("isEnableError: "+LogConfig.getInstance().isEnableError());
		
	}
	
}
