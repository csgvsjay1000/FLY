package ivg.cn.redic.strategy;

public class HashShardingStrategy implements ShardingStrategy{

	@Override
	public <K> int key2Node(K key, int nodeCount) {
		int hashCode = key.hashCode();
		return hashCode%nodeCount;
	}

}
