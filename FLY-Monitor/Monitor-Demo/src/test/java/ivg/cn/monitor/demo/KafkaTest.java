package ivg.cn.monitor.demo;

import java.util.Arrays;
import java.util.Map;

import ivg.cn.monitor.jmx.KafkaJmxConnection;

public class KafkaTest {

	public static void main(String[] args) {

		KafkaJmxConnection connection = new KafkaJmxConnection("192.168.5.131:9988");
		connection.init();
		
		Map<String, Object> result = connection.getValue(null, "kafka.server:type=BrokerTopicMetrics,name=BytesInPerSec", Arrays.asList("Count","OneMinuteRate"));
		System.out.println(result);
		
	}
	
}
