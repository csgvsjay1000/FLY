package ivg.cn.redic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ivg.cn.redic.strategy.HashShardingStrategy;
import ivg.cn.redic.strategy.ShardingStrategy;
import redis.clients.jedis.Jedis;

public class AbstractRedic extends Jedis{

	List<RedicNode> redicNodes = new ArrayList<RedicNode>();
	private ShardingStrategy shardingStrategy = new HashShardingStrategy();
	
	List<String> nodeConnStrs;
	
	
	public void init() {
		for (String connStr : nodeConnStrs) {
			this.addNode(connStr);
		}
	}
	
	private void addNode(String connStr) {
		String[] nodes = connStr.split(RedicNode.NODE_SEPARATOR);
		
		addNode(nodes[0], Arrays.asList(Arrays.copyOf(nodes, 1)));
	}
	
	public AbstractRedic addNode(String jedisConnStr, List<String> slaveConnStrs) {
		redicNodes.add(new RedicNode(jedisConnStr, slaveConnStrs));
		
		return this;
	}
	
	protected <T> Jedis getWriteJedis(T key) {
		// 根据key计算hash, 获得节点索引
		int nodeIndex = this.shardingStrategy.key2Node(key, redicNodes.size());
		RedicNode redicNode = redicNodes.get(nodeIndex);
		return redicNode.getMaster().getResource();
	}
	
	public void setNodeConnStrs(List<String> nodeConnStrs) {
		this.nodeConnStrs = nodeConnStrs;
	}
	
	public List<String> getNodeConnStrs() {
		return nodeConnStrs;
	}
	
}
