package ivg.cn.monitor.redis;

import org.junit.Test;

public class UtilAllTest {

	@Test
	public void testCalucate() {
		
		int used_memory = 912256;  // redis 内部存储使用的总量
		int used_memory_rss = 2211840;  // 应用实际使用内存, 操作系统层面
		
		int rss_fragment = used_memory_rss - used_memory;  // 内存碎片
		
		String humenRssFragment = String.format("%dK", rss_fragment/1024);
		System.out.println(humenRssFragment);
		
		// 内存碎片率
		float memFragmentRatio = (float)used_memory_rss/used_memory;
		System.out.println(memFragmentRatio);
		
		if (memFragmentRatio < 1.0) {
			// 说明 操作系统层面使用的内存小于内部使用总量，这种情况说明使用了swap交换内存,说明max_memory或物理机内存不够，应升级内存或增大max_mmory
			
		}
		
		if (memFragmentRatio > 5.0) { // 使用jemalloc管理内存，减少内存碎片，一般在1.03左右
			// 若实际内存大于内部使用内存 10倍，说明碎片化严重
			// TODO 碎片化严重怎么处理？暂不清楚
			// 数据对齐，安全重启(利用高可用架构)
		}
	}
	
}
