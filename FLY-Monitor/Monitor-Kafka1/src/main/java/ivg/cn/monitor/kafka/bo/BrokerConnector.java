package ivg.cn.monitor.kafka.bo;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.management.remote.JMXConnector;

import org.apache.kafka.clients.admin.AdminClient;

public class BrokerConnector extends ExpireToDelete{
	private JMXConnector jmxConnector;
	
	private AdminClient adminClient;
	
	private volatile boolean valid = true;  //有效无效 
	
	private BrokerItemBean key;
	
	private AtomicInteger faildTimes = new AtomicInteger(0);  // 失败次数
	
	public BrokerConnector() {
		
	}

	public BrokerConnector(JMXConnector jmxConnector, AdminClient adminClient,BrokerItemBean key) {
		super();
		this.jmxConnector = jmxConnector;
		this.adminClient = adminClient;
		this.key = key;
		
	}
	
	public void name() {
		faildTimes.incrementAndGet();
	}
	
	/** 是否超过失败次数限制 */
	public boolean exceedFaildTimeLimit(int limit) {
		return faildTimes.get() >= limit;
	}
	
	/** 是否超过失败次数限制 */
	public boolean exceedFaildTimeLimitAndInc(int limit) {
		return faildTimes.incrementAndGet() >= limit;
	}
	
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public JMXConnector getJmxConnector() {
		return jmxConnector;
	}
	
	public AdminClient getAdminClient() {
		return adminClient;
	}
	
	public BrokerItemBean getKey() {
		return key;
	}
	
	public void close() {
		if (adminClient != null) {
			adminClient.close();
		}
		if (jmxConnector != null) {
			try {
				jmxConnector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
