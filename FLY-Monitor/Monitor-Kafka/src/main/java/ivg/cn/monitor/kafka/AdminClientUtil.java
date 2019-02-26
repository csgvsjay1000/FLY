package ivg.cn.monitor.kafka;

import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.ListTopicsResult;

import ivg.cn.monitor.kafka.service.BrokerService;

public class AdminClientUtil {

	
	public static void listTopics(BrokerConnector brokerConnector, BrokerService brokerService) {
		ListTopicsResult listTopicsResult = brokerConnector.getAdminClient().listTopics();
		 try {
			Set<String> topics = listTopicsResult.names().get();
			if (topics != null) {
				for (String string : topics) {
					brokerService.checkAndPut(string,brokerConnector);
				}
			}
		 } catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
	
}
