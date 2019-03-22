package ivg.cn.monitor.kafka.example.consumer;

public class ConsumerTestMain {

	public static void main(String[] args) throws InterruptedException {
		String brokerList = "192.168.5.131:10200,192.168.5.131:10201";
		String[] topic = {"test_topic_82"};
		String groupId = "invengo_group_2";
		
		ConsumerThreadHandler<byte[], byte[]> handler = 
				new ConsumerThreadHandler<>(brokerList, groupId, topic);
		final int cpuCount = Runtime.getRuntime().availableProcessors();
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				handler.consume(cpuCount);
			}
		};
		new Thread(runnable).start();
		
		System.out.println("The consumer start successful. ");
		while (true) {
			
			Thread.sleep(60000);  // 20s后程序自动停止
		}
//		handler.close();
	}
	
}
