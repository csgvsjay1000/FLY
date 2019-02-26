package ivg.cn.monitor.kafka.example.consumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;

public class ConsumerThreadHandler<K, V> {

	private KafkaConsumer<K, V> consumer;
	private Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
	private ExecutorService executorService;
	
	public ConsumerThreadHandler(String brokerList, String groupId, String... topics) {
		Properties props = new Properties();
		props.put("bootstrap.servers", brokerList);
		props.put("group.id", groupId);
		props.put("enable.auto.commit", false);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		
		consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList(topics), new ConsumerRebalanceListener() {
			// 当broker重新rebalance时触发
			
			@Override
			public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
				// rebalance开始时
				System.out.println("[onPartitionsRevoked] =====  "+partitions);
				consumer.commitSync(offsets);
			}
			
			@Override
			public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
				System.out.println("[onPartitionsAssigned] =====  "+partitions);
				// rebalance结束
				offsets.clear();
			}
		});
		
	}
	
	public void consume(int threadNum) {
		executorService = new ThreadPoolExecutor(threadNum, threadNum, 0, TimeUnit.MILLISECONDS, 
				new ArrayBlockingQueue<>(1000), new ThreadPoolExecutor.CallerRunsPolicy());
		try {
			while (true) {
				ConsumerRecords<K, V> records = consumer.poll(Duration.ofMillis(1000L));
				if (!records.isEmpty()) {
					executorService.submit(new ConsumerWorker<>(records, offsets));
				}
				commitOffsets();
			}
		} catch (WakeupException e) {
			
		}finally {
			commitOffsets();
			consumer.close();
		}
	}
	
	private void commitOffsets() {
		Map<TopicPartition, OffsetAndMetadata> unmodfiedMap;
		synchronized (offsets) {
			if (offsets.isEmpty()) {
				return;
			}
			unmodfiedMap = Collections.unmodifiableMap(new HashMap<>(offsets));
			offsets.clear();
		}
		System.out.println("[commitOffsets] ===== "+unmodfiedMap);
		consumer.commitSync(unmodfiedMap);
	}
	
	public void close() {
		consumer.wakeup();
		executorService.shutdown();
	}
	
}
