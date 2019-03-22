package ivg.cn.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class UtilAll {

	
	public static String date2DayString(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmsss");
		return dateFormat.format(date);
	}
	
	public static <K,V> void mapRemoveItem(Map<K, V> map, MapRemove<K,V> mapRemove) {
		Iterator<Entry<K, V>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<K,V> entry = iter.next();
			if (mapRemove != null) {
				mapRemove.operation(entry, iter);
			}
		}
	}
	
	public interface MapRemove<K,V>{
		void operation(Map.Entry<K,V> entry, Iterator<Entry<K, V>> iter);
	}
	
	public static void iterMapAndRemove(Map<String,ExpireToDelete> map, int timeoutMills) {
		UtilAll.mapRemoveItem(map,new MapRemove<String, ExpireToDelete>() {

			@Override
			public void operation(Entry<String, ExpireToDelete> entry,
					Iterator<Entry<String, ExpireToDelete>> iter) {
				
				if (entry.getValue().expire(timeoutMills)) {
					// 超过3次检测到数据库没有该条记录，即认为这条记录被删除
					
					iter.remove();
				}
			}
		});
	}
	
	public static String dateFormat(long time) {
		Date date = new Date(time);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:SSS");
		return dateFormat.format(date);
	}
}
