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
	
	public static <K,V> void mapRemoveItem(Map<K, V> map, int timeoutMills, MapRemove<K,V> mapRemove) {
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
	
}
