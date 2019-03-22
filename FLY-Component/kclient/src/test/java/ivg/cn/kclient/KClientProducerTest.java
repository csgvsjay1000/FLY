package ivg.cn.kclient;

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
		 props.put("bootstrap.servers", "192.168.5.131:10200");
		 props.put("acks", "all");
		 props.put("retries", 0);
		 props.put("batch.size", 16384);
		 props.put("linger.ms", 1);
		 props.put("buffer.memory", 33554432);
		 props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		 props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		 
		 Producer<String, String> producer = new KafkaProducer<String, String>(props);
		 producer.send(new ProducerRecord<String, String>("test-topic", "3434", "t43e"));
		 producer.flush();
//		 for (int i = 0; i < 100; i++)
//		     producer.send(new ProducerRecord<String, String>("test-topic", Integer.toString(i), Integer.toString(i)));

		 producer.close();
	}
	
	@Test
	public void testConsumer() {
		Properties props = new Properties();
	     props.put("bootstrap.servers", "192.168.5.131:10200");
	     props.put("group.id", "rwetwetwe");
	     props.put("enable.auto.commit", "true");
	     props.put("auto.commit.interval.ms", "1000");
	     props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	     props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	     KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
	     consumer.subscribe(Arrays.asList("delivery_topic"));
	     while (true) {
	    	 
	         ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
	         for (ConsumerRecord<String, String> record : records)
	             System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
	     }
	}

	public static void main(String[] args) {
		
		KClientProducer producer = new KClientProducer("kafka-producer.properties");
		producer.send2Topic("test-topic", "key", "test-message");
	}
	
	@Test
	public void testP1() {
		KClientProducer producer = new KClientProducer("kafka-producer.properties");
		producer.send2Topic("test-topic", "key", "test-message");
		
	}
	
}
