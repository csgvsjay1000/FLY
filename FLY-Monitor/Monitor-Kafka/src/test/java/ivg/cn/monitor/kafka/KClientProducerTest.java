package ivg.cn.monitor.kafka;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;

public class KClientProducerTest {

	@Test
	public void testProducer() {
		Properties props = new Properties();
		 props.put("bootstrap.servers", "192.168.5.131:10202");
		 props.put("acks", "all");  // all或-1表示所有ISR中副本都写入成功后，才返回提交成功
		 props.put("retries", 0);
		 props.put("batch.size", 16384);
		 props.put("linger.ms", 1);
		 props.put("buffer.memory", 33554432);
		 props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		 props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		 
		 Producer<String, String> producer = new KafkaProducer<String, String>(props);
//		 producer.send(new ProducerRecord<String, String>("inventory_topic", "inventory_topic_key_2", "inventory_topic_value_2"));
//		 producer.flush();
		 for (int i = 0; i < 10; i++){
			 
			 producer.send(new ProducerRecord<String, String>("inventory_topic", "inventory_topic_key_"+i, "inventory_topic_value_2"+i));
			 try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }

		 try {
			Thread.sleep(60*1000*30);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// sleep 30s
		 producer.close();
	}
	
	@Test
	public void testConsumer() {
		Properties props = new Properties();
	     props.put("bootstrap.servers", "192.168.5.131:10202");
	     props.put("group.id", "test");
	     props.put("enable.auto.commit", "true");
	     props.put("auto.commit.interval.ms", "1000");
	     props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	     props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	     KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
	     consumer.subscribe(Arrays.asList("inventory_topic"));
	     while (true) {
	    	 
	         ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
	         for (ConsumerRecord<String, String> record : records){
	        	 System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
	        	 try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	         }
	     }
	}

	
}
