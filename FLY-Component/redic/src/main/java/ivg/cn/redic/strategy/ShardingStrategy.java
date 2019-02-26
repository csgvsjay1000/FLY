package ivg.cn.redic.strategy;

/**
 * 分片策略
 * */
public interface ShardingStrategy {

	<K> int key2Node(K key, int nodeCount);
	
}
