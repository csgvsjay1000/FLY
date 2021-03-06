package ivg.cn.print.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ivg.cn.common.DreamResponse;
import ivg.cn.print.dto.PrintDetailDTO;
import ivg.cn.vesta.ConcurrentTestTools;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=PrintServiceStartup.class)
public class TestSprint {

	@Autowired
	PrintService printService;
	
	@Test
	public void testInsert() {
		
	}
	
	@Test
	public void testGenId() {
		
		Map<Long, Integer> map = new ConcurrentHashMap<>();
		
		Runnable task = new Runnable() {
			
			@Override
			public void run() {
				long start = System.currentTimeMillis();
				for (int i = 0; i < 1000; i++) {
					DreamResponse<String> response = printService.genPrintId();
					long id = (long) response.getExtData().get("id");
					if (map.containsKey(id)) {
						throw new RuntimeException("id duplicate error: "+id);
//						Integer v = map.get(id);
//						v++;
//						map.put(id, v);
					}else {
						map.put(id, 1);
					}
					insertTo(id, response.getObjData());
//					try {
//						Thread.sleep(100);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
					
				}
				System.out.println(String.format("cost %dms", (System.currentTimeMillis()-start)));
			}
		};
		
		ConcurrentTestTools testTools = new ConcurrentTestTools(100, task);
		testTools.start();
		
		for (Entry<Long, Integer> entry : map.entrySet()) {
			Long key = entry.getKey();
			Integer value = entry.getValue();
			if (value > 1) {
				System.out.println(key+": "+value);
			}
		}
		
	}
	
	private void insertTo(long id, String num) {
		PrintDetailDTO detailDTO = new PrintDetailDTO();
		
		detailDTO.setMerchantId(100L);
		detailDTO.setPrintId(id);
		detailDTO.setOperatorId(2L);
		List<PrintDetailDTO.InerDetail> inerDetails = new ArrayList<>();
		for(int i=0; i <4;i++){
			PrintDetailDTO.InerDetail inerDetail = new PrintDetailDTO.InerDetail();
			inerDetail.setAssertUnit("包");
			inerDetail.setCommodityAssetBarcode("SP"+i);
			inerDetail.setCommodityAssetId(i+2390L);
			inerDetail.setPrintQty(20);
			inerDetail.setNum("SP"+345);
			inerDetail.setPrintId(id);
			inerDetails.add(inerDetail);
		}
		detailDTO.setDetailItems(inerDetails);
		printService.insertPrintDetail(detailDTO);
	}
	
}
