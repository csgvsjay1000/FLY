package ivg.cn.monitor.kafka;

import java.math.BigDecimal;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

public class BrokerJmxUtil {

	
	public static void byteInPerSec(MBeanServerConnection connection) {
		
		String objectNameStr = "kafka.server:type=BrokerTopicMetrics,name=BytesInPerSec";
		try {
			ObjectName objectName = new ObjectName(objectNameStr);
			 long begin = System.currentTimeMillis();
			AttributeList attributeList = connection.getAttributes(objectName, 
					new String[] {"MeanRate","OneMinuteRate","FifteenMinuteRate"});
			System.out.println("getAttributes cost="+(System.currentTimeMillis()-begin));
			if (attributeList != null) {
				for (Object object : attributeList) {
					if (object instanceof Attribute) {
						Attribute map = (Attribute) object;
						Double MeanRate = (Double) map.getValue();
						BigDecimal bigDecimal = BigDecimal.valueOf(MeanRate);
						bigDecimal = bigDecimal.setScale(2,BigDecimal.ROUND_HALF_EVEN);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
