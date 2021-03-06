package ivg.cn.redic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import ivg.cn.redic.strategy.RoundRobinSelectStrategy;
import ivg.cn.redic.strategy.SelectStrategy;
import redis.clients.jedis.JedisPool;

public class RedicNode {
	public static final String NODE_SEPARATOR = ",";
	public static final String HOST_PORT_SEPARATOR = ":";
	
	private JedisPool master;
	private List<JedisPool> slaves;
	
	private SelectStrategy selectStrategy;
	
	public RedicNode() {
		this.selectStrategy = new RoundRobinSelectStrategy();
	}
	
	public RedicNode(String masterConnStr, List<String> slaveConnStrs) {
		String[] masterHostPortArray = masterConnStr.split(HOST_PORT_SEPARATOR);
		this.master = new JedisPool(new GenericObjectPoolConfig(), 
				masterHostPortArray[0],Integer.valueOf(masterHostPortArray[1]));
		
		this.slaves = new ArrayList<JedisPool>();
		for(String slaveConnStr  : slaveConnStrs){
			String[] slaveHostPortArray = slaveConnStr.split(HOST_PORT_SEPARATOR);
			slaves.add(new JedisPool(new GenericObjectPoolConfig(), 
					slaveHostPortArray[0],Integer.valueOf(slaveHostPortArray[1])));
		}
		
		this.selectStrategy = new RoundRobinSelectStrategy();
	}

	public JedisPool getRoundRobinSlaveRedicNode() {
		int index = selectStrategy.select(slaves.size());
		return slaves.get(index);
	}
	
	public JedisPool getMaster() {
		return master;
	}

	public void setMaster(JedisPool master) {
		this.master = master;
	}

	public List<JedisPool> getSlaves() {
		return slaves;
	}

	public void setSlaves(List<JedisPool> slaves) {
		this.slaves = slaves;
	}
	
}
