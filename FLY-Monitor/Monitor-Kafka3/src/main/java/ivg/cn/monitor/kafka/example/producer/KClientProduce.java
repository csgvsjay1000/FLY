package ivg.cn.monitor.kafka.example.producer;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * 多线程共享单实例
 * */
public class KClientProduce<K, V> {

	KafkaProducer<K, V> producer;
	
	public KClientProduce() {
		Properties props = new Properties();
		props.put("bootstrap.servers", "192.168.5.131:10201");
		props.put("acks", "all");  // all或-1表示所有ISR中副本都写入成功后，才返回提交成功
		props.put("retries", Integer.MAX_VALUE);  // 可重试异常的重试次数
		props.put("batch.size", 16384);  // 每个分区缓冲池大小单位byte，当缓冲池满了之后produce会发送到broker端
		props.put("linger.ms", 100);  // 消息发送延时，单位毫秒，表示即使缓冲池没满，当消息滞留缓冲池时间超过该值时就会被发送
		props.put("max.block.ms", 3000);  // 当内存缓冲区满了，produce将阻塞发送线程，等io线程发送数据出去之后再继续发送，阻塞时间
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		producer = new KafkaProducer<>(props);
	}
	
	public void send(String topic, K key, V value, Callback callback) {
		producer.send(new ProducerRecord<K, V>(topic, key, value), callback);
	}
	
	public void close() {
		producer.close();
	}
}
