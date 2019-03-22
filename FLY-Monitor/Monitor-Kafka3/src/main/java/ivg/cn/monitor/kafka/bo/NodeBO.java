package ivg.cn.monitor.kafka.bo;


/**
 * 
 * */
public class NodeBO {

	private final int id;
	
	private String host;
	
    private int port;
	
	private int jmxPort;
	
	public NodeBO(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public int getJmxPort() {
		return jmxPort;
	}

	public void setJmxPort(int jmxPort) {
		this.jmxPort = jmxPort;
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return String.format("id=%d, broker=%s:%d, jmxPort=%d", id,host,port,jmxPort);
	}
	
}
