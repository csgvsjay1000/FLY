package ivg.cn.monitor.kafka.example.producer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.RetriableException;

public class ProduceTestMain {

	public static void main(String[] args) throws InterruptedException {
		KClientProduce<String, String> produce = new KClientProduce<>();
		produce.send("inventory_topic", null, "hello", new Callback() {
			
			@Override
			public void onCompletion(RecordMetadata metadata, Exception exception) {
				if (exception == null) {
					// 发送成功 
					System.out.println("发送成功");
				}else {
					exception.printStackTrace();
					// 发送失败
					if (exception instanceof RetriableException) {
						System.out.println("可重试异常");
						
					}else {
						System.out.println("不可重试异常");
						
					}
					
				}
			}
		});
		
		System.out.println("The producer start successful. ");
		
		Thread.sleep(20000);  // 20s后程序自动停止
		System.out.println("Starting to close the producer...");
		produce.close();
	}
	
}
