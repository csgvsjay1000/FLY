package ivg.cn.print.service.consumer;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ivg.cn.common.ExecutorUtils;
import ivg.cn.kclient.MessageHandler;
import ivg.cn.print.service.PrintConst;

@Component
public class DefaultConsumer implements MessageHandler{

	@Autowired
	private PrintEpcConsumer printEpcConsumer;

	ThreadPoolExecutor businessPool = null;
	
	public DefaultConsumer() {
		businessPool = ExecutorUtils.traceFixedThreadPool("print-kafka-consumer", 10);
	}
	
	@Override
	public void execute(String topic, String key, String message) {
		
		businessPool.submit(new Runnable() {
			
			@Override
			public void run() {
				if (PrintConst.KafkaPrintEpcTopic.equals(topic)) {
					printEpcConsumer.execute(topic, key, message);
				}
			}
		});
		
	}

}
