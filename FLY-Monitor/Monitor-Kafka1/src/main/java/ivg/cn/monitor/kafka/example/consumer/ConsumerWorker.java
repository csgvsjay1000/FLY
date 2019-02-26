package ivg.cn.monitor.kafka.example.consumer;

import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

public class ConsumerWorker<K, V> implements Runnable{

	private final ConsumerRecords<K, V> records;
	private final Map<TopicPartition, OffsetAndMetadata> offsets;
	
	public ConsumerWorker(ConsumerRecords<K, V> records, Map<TopicPartition, OffsetAndMetadata> offsets) {
		this.records = records;
		this.offsets = offsets;
	}
	
	@Override
	public void run() {
		if (records == null) {
			return;
		}
		for(TopicPartition partition : records.partitions()){
			List<ConsumerRecord<K, V>> consumerRecords = records.records(partition);
			for (ConsumerRecord<K, V> consumerRecord : consumerRecords) {
				// TODO 消息逻辑处理
				System.out.println(String.format("topic=%s, partition=%d, offset=%d", 
						consumerRecord.topic(), consumerRecord.partition(), consumerRecord.offset()));
			}
			
			// 上报位移消息
			// 获取最后一条记录的位移
			long lastOffset = consumerRecords.get(consumerRecords.size()-1).offset();
			synchronized (offsets) {
				if (!offsets.containsKey(partition)) {
					offsets.put(partition, new OffsetAndMetadata(lastOffset+1));
				}else {
					long curr = offsets.get(partition).offset();
					if (curr <= lastOffset+1) {
						offsets.put(partition, new OffsetAndMetadata(lastOffset+1));
					}
				}
			}
		}
		
	}
	
}
