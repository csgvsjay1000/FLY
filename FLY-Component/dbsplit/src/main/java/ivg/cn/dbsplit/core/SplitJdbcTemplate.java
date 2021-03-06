package ivg.cn.dbsplit.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import ivg.cn.dbsplit.LogConfig;
import ivg.cn.dbsplit.core.sql.DatabaseTableVO;
import ivg.cn.dbsplit.core.sql.parse.SplitSqlParser;
import ivg.cn.dbsplit.core.sql.parse.SplitSqlStructure;
import ivg.cn.dbsplit.core.sql.util.OrmUtil;

public class SplitJdbcTemplate implements SplitJdbcOperations{
	protected static Logger log = LoggerFactory.getLogger(SplitJdbcTemplate.class);
	
	protected SplitTablesHolder splitTablesHolder;
	
	SplitActionRunner splitActionRunner = new SplitActionRunner();
	
	@Override
	public <T, K> T execute(K splitKey, ConnectionCallback<T> action) throws DataAccessException {
		return null;
	}
	
	@Override
	public <K, T> List<T> query(K splitKey, String sql, Object[] args, RowMapper<T> rowMapper) {

		return splitActionRunner.runSplitAction(splitKey, sql, new SplitAction<List<T>>() {

			@Override
			public List<T> doSplitAction(JdbcTemplate jt, String sql) {
				return jt.query(sql, args, rowMapper);
			}
		});
	}
	
	protected JdbcTemplate getWriteJdbcTemplate(SplitNode splitNode) {
		return getJdbcTemplate(splitNode, false);
	}

	protected JdbcTemplate getJdbcTemplate(SplitNode splitNode, boolean read) {
		if (!read) {
			// 若不是读节点
			return splitNode.getMasterTemplate();
		}
		return splitNode.getSlaveTemplates().get(0);
	}

	public SplitTablesHolder getSplitTablesHolder() {
		return splitTablesHolder;
	}

	public void setSplitTablesHolder(SplitTablesHolder splitTablesHolder) {
		this.splitTablesHolder = splitTablesHolder;
	}
	
	interface SplitAction<T>{
		T doSplitAction(JdbcTemplate jt, String sql);
	}
	
	public class SplitActionRunner{
		
		<K,T> T runSplitAction(K splitKey, String sql, SplitAction<T> splitAction){
			
			SplitSqlStructure sqlStruct = SplitSqlParser.INST.parse(sql);
			
//			String dbName = sqlStruct.getDbName();
			String tableName = sqlStruct.getTableName();
			
			DatabaseTableVO vo = obtainDbAndTb(splitKey, null, null, tableName);
			
			String splitSql = sqlStruct.getSplitSql(vo.getDbIndex(), vo.getTableIndex());
			
			return splitAction.doSplitAction(vo.getSn().getMasterTemplate(), splitSql);
		}
		
	}
	
	protected <K,T> DatabaseTableVO obtainDbAndTb(K splitKey, Class<T> clazz, T bean) {
		return obtainDbAndTb(splitKey, clazz, bean, null);
	}
	
	protected <K,T> DatabaseTableVO obtainDbAndTb(K splitKey, Class<T> clazz, T bean,String tableName) {
		String simpleName = null;
		SplitTable splitTable = null;
		if (tableName != null) {
			splitTable = splitTablesHolder.searchSplitTable(tableName);
		}else {
			if (clazz == null) {
				simpleName = bean.getClass().getSimpleName();
			}else {
				simpleName = clazz.getSimpleName();
			}
			splitTable = splitTablesHolder.searchSplitTable(OrmUtil.javaClassName2DbTableName(simpleName));
		}
		
		
		SplitStrategy splitStrategy = splitTable.getSplitStrategy();
		List<SplitNode> splitNodes = splitTable.getSplitNodes();
		
		String dbPrefix = splitTable.getDbNamePrefix();
		String tablePrefix = splitTable.getTableNamePrefix();
		
		if (!splitTable.isSplit()) {
			// 如果不是分片表
			SplitNode sn = splitNodes.get(0);
			return new DatabaseTableVO(dbPrefix, tablePrefix, sn);
		}else {
			// 分片表
			int nodeNo = splitStrategy.getNodeNo(splitKey);
			int dbNo = splitStrategy.getDbNo(splitKey);
			int tableNo = splitStrategy.getTableNo(splitKey);
			
			if (LogConfig.getInstance().isEnableDebug()) {
				log.info(
						"SimpleSplitJdbcTemplate.doSearch, splitKey={} dbPrefix={} tablePrefix={} nodeNo={} dbNo={} tableNo={}.",
						splitKey, dbPrefix, tablePrefix, nodeNo, dbNo, tableNo);
			}
			SplitNode sn = splitNodes.get(nodeNo);
			
			return new DatabaseTableVO(dbPrefix, tablePrefix, dbNo, tableNo, sn);
		}
	}
	
}
