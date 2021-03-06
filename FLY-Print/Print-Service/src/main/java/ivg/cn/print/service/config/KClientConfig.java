package ivg.cn.print.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ivg.cn.kclient.KClientConsumer;
import ivg.cn.kclient.KClientProducer;
import ivg.cn.print.service.PrintConst;
import ivg.cn.print.service.consumer.DefaultConsumer;

@Configuration
public class KClientConfig {

	@Autowired
	private DefaultConsumer defaultConsumer;
	
	@Bean
	public KClientProducer genProducer() {
		KClientProducer producer = new KClientProducer("kafka-producer.properties");
		return producer;
	}
	
	@Bean
	public KClientConsumer genConsumer() {
		KClientConsumer consumer = new KClientConsumer("kafka-consumer.properties");
		consumer.setHandler(defaultConsumer);
		consumer.subscribeTopic(PrintConst.KafkaPrintEpcTopic);
		consumer.start();
		return consumer;
	}
	
}
