package ivg.cn.monitor.kafka.bo;

import ivg.cn.monitor.kafka.entity.BrokerItem;

public class BrokerItemBean {
	private String brokerIp;

    private int brokerPort;

    private int jmxPort;
    
    int hash = -1;
    
    public static BrokerItemBean create(BrokerItem brokerItem) {
		BrokerItemBean itemBean = new BrokerItemBean();
		itemBean.setBrokerIp(brokerItem.getBrokerIp());
		itemBean.setBrokerPort(brokerItem.getBrokerPort());
		itemBean.setJmxPort(brokerItem.getJmxPort());
		return itemBean;
	}

	public String getBrokerIp() {
		return brokerIp;
	}

	public void setBrokerIp(String brokerIp) {
		this.brokerIp = brokerIp;
	}

	public Integer getBrokerPort() {
		return brokerPort;
	}

	public void setBrokerPort(Integer brokerPort) {
		this.brokerPort = brokerPort;
	}

	public Integer getJmxPort() {
		return jmxPort;
	}

	public void setJmxPort(Integer jmxPort) {
		this.jmxPort = jmxPort;
	}
    
	@Override
	public boolean equals(Object obj) {
		if (null == obj) {
			return false;
		}
		if (!(obj instanceof BrokerItemBean)) {
			return false;
		}
		BrokerItemBean targetObj = (BrokerItemBean) obj;
		
		// 判断IP是否相等
		boolean ipEqual = false;
		if (this.brokerIp == null) {
			if (targetObj.getBrokerIp() == null) {
				ipEqual = true;
			}else {
				ipEqual = false;
			}
		}else {
			ipEqual = this.brokerIp.equals(targetObj.getBrokerIp());
		}
		
		// 判断 port是否相等
		boolean portEqual = this.brokerPort == targetObj.getBrokerPort();
		boolean jmxPortEqual = this.jmxPort == targetObj.getJmxPort();
		
		return ipEqual && portEqual && jmxPortEqual;
	}
	
    @Override
    public int hashCode() {
    	if (this.hash != -1) {
			return hash;
		}
    	int h = 17;
		h = h*31 + brokerIp.hashCode();
		h = h*31 + brokerPort;
		h = h*31 + jmxPort;
		this.hash = h;
		return h;
    }
    
    @Override
    public String toString() {
    	return "brokerIp="+brokerIp+", brokerPort="+brokerPort+", jmxPort="+jmxPort;
    }
    
    
}
