package ivg.cn.dbsplit.core.sql;

import ivg.cn.dbsplit.core.SplitNode;
import ivg.cn.dbsplit.core.sql.util.SqlUtil;

public class DatabaseTableVO {
	final String dbPrefix;
	
	final String tablePrefix;
	
	int dbIndex;
	
	int tableIndex;
	
	final SplitNode sn;
	
	boolean split = true;  //是否分片，默认分片
	
	/**
	 * 创建不分片表
	 * */
	public DatabaseTableVO(String dbPrefix, String tablePrefix, SplitNode sn) {
		this(dbPrefix, tablePrefix, 0, 0, sn);
		this.split = false;
	}
	
	public DatabaseTableVO(String dbPrefix, String tablePrefix, int dbIndex, int tableIndex, SplitNode sn) {
		super();
		this.dbPrefix = dbPrefix;
		this.tablePrefix = tablePrefix;
		this.dbIndex = dbIndex;
		this.tableIndex = tableIndex;
		this.sn = sn;
	}

	public String getDbPrefix() {
		return dbPrefix;
	}

	public String getTablePrefix() {
		return tablePrefix;
	}

	public int getDbIndex() {
		return dbIndex;
	}

	public int getTableIndex() {
		return tableIndex;
	}

	public SplitNode getSn() {
		return sn;
	}
	
	public void setSplit(boolean split) {
		this.split = split;
	}

	public boolean isSplit() {
		return split;
	}
	
	public String getPhysicalTableName() {
		if (split) {
			return SqlUtil.getQualifiedTableName(dbPrefix, tablePrefix, dbIndex, tableIndex);
		}else {
			return SqlUtil.getQualifiedTableName(dbPrefix, tablePrefix);
		}
	}
}
