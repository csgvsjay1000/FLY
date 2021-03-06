package ivg.cn.dbsplit.core;

/**
 * 水平hash分片策略
 * */
public class HorizontalHashSplitStrategy implements SplitStrategy{

	private int portNum;
	private int dbNum;
	private int tableNum;
	
	public HorizontalHashSplitStrategy(int portNum, int dbNum, int tableNum) {
		super();
		this.portNum = portNum;
		this.dbNum = dbNum;
		this.tableNum = tableNum;
	}

	@Override
	public <K> int getNodeNo(K splitKey) {
		return getDbNo(splitKey)/dbNum;
	}

	@Override
	public <K> int getDbNo(K splitKey) {
		return getTableNo(splitKey)/tableNum;
	}

	@Override
	public <K> int getTableNo(K splitKey) {
		int hashCode = calcHashCode(splitKey);
		return hashCode % (portNum * dbNum * tableNum);
	}
	
	private int calcHashCode(Object splitKey) {
		int hashCode = splitKey.hashCode();
		if (hashCode < 0) {
			hashCode = -hashCode;
		}
		return hashCode;
	}

}
