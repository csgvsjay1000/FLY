package ivg.cn.vesta;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;

import ivg.cn.vesta.impl.IdServiceImpl;

public class IdServiceTest {

	
	@Test
	public void testId() {
		IdService idService = new IdServiceImpl();
		
		long start = System.currentTimeMillis();
		Map<Long, Integer> map = new HashMap<>();
		for (int i = 0; i < 100000; i++) {
			long id = idService.genId();
			if (map.containsKey(id)) {
				Integer v = map.get(id);
				v++;
				map.put(id, v);
			}else {
				map.put(id, 1);
			}
		}
		System.out.println(String.format("cost %dms", (System.currentTimeMillis()-start)));
		
		for (Entry<Long, Integer> entry : map.entrySet()) {
			Long key = entry.getKey();
			Integer value = entry.getValue();
			if (value > 1) {
				System.out.println(key+": "+value);
			}
		}
	}
	
	@Test
	public void testId_1() {
		IdService idService = new IdServiceImpl();
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			long id = idService.genId();
			Id idBean = idService.expId(id);
			System.out.println("id="+id+", "+idBean);
		}
		System.out.println(String.format("cost %dms", (System.currentTimeMillis()-start)));
		
	}
	
	public static void main(String[] args) {
		IdServiceImpl idService = new IdServiceImpl();
		idService.setMachineId(11);
		Map<Long, Integer> map = new ConcurrentHashMap<>();
		
		Runnable task = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				long start = System.currentTimeMillis();
				for (int i = 0; i < 100000; i++) {
					long id = idService.genId();
					if (map.containsKey(id)) {
						Integer v = map.get(id);
						v++;
						map.put(id, v);
					}else {
						map.put(id, 1);
					}
					
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
	
}
