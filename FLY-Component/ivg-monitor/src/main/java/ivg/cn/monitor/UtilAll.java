package ivg.cn.monitor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilAll {

	public static String dateFormat(long time) {
		Date date = new Date(time);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:SSS");
		return dateFormat.format(date);
	}
	
}
