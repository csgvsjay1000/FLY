package ivg.cn.monitor.redis.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilAll {

	
	public static String date2DayString(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmsss");
		return dateFormat.format(date);
	}
	
}
