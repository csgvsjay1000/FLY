package ivg.cn.dbsplit.core;

import java.util.List;

public class SplitTable {

	private String dbNamePrefix;
	private String tableNamePrefix;
	
	private int dbNum;
	private int tableNum;
	
	private boolean split = true;  // 是否为分片表, 默认分片表
	
	private SplitStrategyType splitStrategyType = SplitStrategyType.VERTICAL;
	private SplitStrategy splitStrategy;
	private List<SplitNode> splitNodes;
	
	boolean readWriteSeparate = true;  // 读写分离
	
	public void init() {
		if (splitStrategyType == SplitStrategyType.HORIZONTAL) {
			this.splitStrategy = new HorizontalHashSplitStrategy(splitNodes.size(), dbNum, tableNum);
		}
	}

	public String getDbNamePrefix() {
		return dbNamePrefix;
	}

	public void setDbNamePrefix(String dbNamePrefix) {
		this.dbNamePrefix = dbNamePrefix;
	}

	public String getTableNamePrefix() {
		return tableNamePrefix;
	}

	public void setTableNamePrefix(String tableNamePrefix) {
		this.tableNamePrefix = tableNamePrefix;
	}

	public int getDbNum() {
		return dbNum;
	}

	public void setDbNum(int dbNum) {
		this.dbNum = dbNum;
	}

	public int getTableNum() {
		return tableNum;
	}

	public void setTableNum(int tableNum) {
		this.tableNum = tableNum;
	}

	public SplitStrategyType getSplitStrategyType() {
		return splitStrategyType;
	}

	public void setSplitStrategyType(String value) {
		this.splitStrategyType = SplitStrategyType.valueOf(value);
	}

	public List<SplitNode> getSplitNodes() {
		return splitNodes;
	}

	public void setSplitNodes(List<SplitNode> splitNodes) {
		this.splitNodes = splitNodes;
	}

	public boolean isReadWriteSeparate() {
		return readWriteSeparate;
	}

	public void setReadWriteSeparate(boolean readWriteSeparate) {
		this.readWriteSeparate = readWriteSeparate;
	}
	
	public SplitStrategy getSplitStrategy() {
		return splitStrategy;
	}

	public void setSplitStrategy(SplitStrategy splitStrategy) {
		this.splitStrategy = splitStrategy;
	}

	public boolean isSplit() {
		return split;
	}

	public void setSplit(boolean split) {
		this.split = split;
	}
	
}
