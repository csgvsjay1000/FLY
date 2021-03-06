package ivg.cn.dbsplit.core;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ivg.cn.dbsplit.LogConfig;

public class SplitTablesHolder {
	private static Logger log = LoggerFactory.getLogger(LogConfig.LOG_NAME);
	private static final String DB_TABLE_SEP = "$";
	private List<SplitTable> splitTables;
	
	private HashMap<String, SplitTable> splitTablesMapFull;
	private HashMap<String, SplitTable> splitTablesMap;
	
	public void init() {
		splitTablesMapFull = new HashMap<>();
		splitTablesMap = new HashMap<>();
		
		log.info("------- initialize split tables --------");
		
		for (SplitTable splitTable : splitTables) {
			String key = this.constructKey(splitTable.getDbNamePrefix(), splitTable.getTableNamePrefix());
			
			splitTablesMapFull.put(key, splitTable);
			splitTablesMap.put(splitTable.getTableNamePrefix(), splitTable);
			log.info("------- {} ",key);
		}
		log.info("----------------------------------------");
	}
	
	public SplitTable searchSplitTable(String tableName) {
		return splitTablesMap.get(tableName);
	}
	
	public SplitTable searchSplitTable(String dbName,String tableName) {
		return splitTablesMapFull.get(tableName);
	}

	public List<SplitTable> getSplitTables() {
		return splitTables;
	}

	public void setSplitTables(List<SplitTable> splitTables) {
		this.splitTables = splitTables;
	}
	
	private String constructKey(String dbName, String tableName) {
		return dbName+DB_TABLE_SEP+tableName;
	}
	
	
}
