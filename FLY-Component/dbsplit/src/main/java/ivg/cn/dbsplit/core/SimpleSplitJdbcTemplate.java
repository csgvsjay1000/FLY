package ivg.cn.dbsplit.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import ivg.cn.dbsplit.LogConfig;
import ivg.cn.dbsplit.RowBounds;
import ivg.cn.dbsplit.core.sql.DatabaseTableVO;
import ivg.cn.dbsplit.core.sql.Example;
import ivg.cn.dbsplit.core.sql.SqlRunningBean;
import ivg.cn.dbsplit.core.sql.util.OrmUtil;
import ivg.cn.dbsplit.core.sql.util.SqlUtil;

public class SimpleSplitJdbcTemplate extends SplitJdbcTemplate implements SimpleSplitJdbcOperations{

	private enum UpdateOper{
		INSERT,UPDATE,DELETE,BATCH_INSERT
	}
	
	private enum SearchOper{
		NORMAL,RANGE,FIELD
	}
	
	@Override
	public <K, T> int insert(K splitKey, T bean) {
		return doUpdate(splitKey, bean.getClass(), UpdateOper.INSERT, bean, -1);
	}
	
	@Override
	public <K, T> int delete(K splitKey, long id, Class<T> clazz) {
		return doUpdate(splitKey, clazz, UpdateOper.DELETE, null, id);
	}
	
	@Override
	public <K, T> List<T> select(K splitKey, T bean) {
		return doSearch(splitKey, bean, null, null, null, SearchOper.NORMAL);
	}
	
	@Override
	public <K, T> List<T> selectAll(K splitKey, Class<T> clazz) {
		return doSearch(splitKey, null,clazz, null, null, null, SearchOper.NORMAL, 0, 0);
	}
	
	@Override
	public <K, T> List<T> selectPages(K splitKey, T bean, int offset, int limit) {
		return doSearch(splitKey, bean,null, null, null, null, SearchOper.NORMAL,offset,limit);
	}
	
	@Override
	public <K, T> int batchInsert(K splitKey, List<T> beans) {
		DatabaseTableVO db = obtainDbAndTb(splitKey, null,beans.get(0));
		JdbcTemplate jdbcTemplate = getWriteJdbcTemplate(db.getSn());
		SqlRunningBean srb = SqlUtil.generateBatchInsertSql(beans, db.getPhysicalTableName());
		if (LogConfig.getInstance().isEnableDebug()) {
			log.info("SimpleSplitJdbcTemplate.doUpdate, the split sql:{}, params:{}",srb.getSql(),srb.getParams());
		}
		int updateCount = jdbcTemplate.update(srb.getSql(), srb.getParams());
		if (LogConfig.getInstance().isEnableDebug()) {
			log.info("SimpleSplitJdbcTemplate.batchInsert, updateCount:{}",updateCount);
		}
		return updateCount;
	}
	
	@Override
	public <K, T> T selectOne(K splitKey, long id, Class<T> clazz) {
		DatabaseTableVO db = obtainDbAndTb(splitKey, clazz,null);
		JdbcTemplate jdbcTemplate = getWriteJdbcTemplate(db.getSn());
		
		SqlRunningBean srb = SqlUtil.generateSelectSqlWithId(id, db.getDbPrefix(), db.getTablePrefix(), db.getDbIndex(), db.getTableIndex());
		if (LogConfig.getInstance().isEnableDebug()) {
			log.info("SimpleSplitJdbcTemplate.doUpdate, the split sql:{}, params:{}",srb.getSql(),srb.getParams());
		}
		try {
			return jdbcTemplate.queryForObject(srb.getSql(), srb.getParams(), new RowMapper<T>() {
				@Override
				public T mapRow(ResultSet arg0, int arg1) throws SQLException {
					return (T) OrmUtil.convertRow2Bean(arg0, clazz);
				}
			});
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	@Override
	public <K, T> T selectOne(K splitKey, T bean) {
		DatabaseTableVO db = obtainDbAndTb(splitKey, null,bean);
		JdbcTemplate jdbcTemplate = getWriteJdbcTemplate(db.getSn());
		
		SqlRunningBean srb = SqlUtil.generateSearchSql(bean, db.getPhysicalTableName(), 0, 0);
		
		if (LogConfig.getInstance().isEnableDebug()) {
			log.info("SimpleSplitJdbcTemplate.doUpdate, the split sql:{}, params:{}",srb.getSql(),srb.getParams());
		}
		try {
			return jdbcTemplate.queryForObject(srb.getSql(), srb.getParams(), new RowMapper<T>() {

				@SuppressWarnings("unchecked")
				@Override
				public T mapRow(ResultSet arg0, int arg1) throws SQLException {
					return (T) OrmUtil.convertRow2Bean(arg0, bean.getClass());
				}
			});
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	@Override
	public <K, T> List<T> select(K splitKey, Example example) {
		DatabaseTableVO db = obtainDbAndTb(splitKey, example.getClazz(),null);
		JdbcTemplate jdbcTemplate = getWriteJdbcTemplate(db.getSn());
		
		SqlRunningBean srb = example.generateSql(db);
		
		if (LogConfig.getInstance().isEnableDebug()) {
			log.info("SimpleSplitJdbcTemplate.select, the split sql:{}, params:{}",srb.getSql(),srb.getParams());
		}
		List<T> beans = jdbcTemplate.query(srb.getSql(), srb.getParams(), new RowMapper<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T mapRow(ResultSet arg0, int arg1) throws SQLException {
				return (T) OrmUtil.convertRow2Bean(arg0, example.getClazz());
			}
		});
		return beans;
	}
	
	@Override
	public <K, T> List<T> select(K splitKey, Example example, RowBounds rowBounds) {
		DatabaseTableVO db = obtainDbAndTb(splitKey, example.getClazz(),null);
		JdbcTemplate jdbcTemplate = getWriteJdbcTemplate(db.getSn());
		
		SqlRunningBean srb = example.generateSql(db);
		
		if (LogConfig.getInstance().isEnableDebug()) {
			log.info("SimpleSplitJdbcTemplate.select, the split sql:{}, params:{}",srb.getSql(),srb.getParams());
		}
		List<T> beans = jdbcTemplate.query(srb.getSql(), srb.getParams(), new RowMapper<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T mapRow(ResultSet arg0, int arg1) throws SQLException {
				return (T) OrmUtil.convertRow2Bean(arg0, example.getClazz());
			}
		});
		return beans;
	}
	
	@Override
	public <K> Integer count(K splitKey, Example example) {
		DatabaseTableVO db = obtainDbAndTb(splitKey, example.getClazz(),null);
		JdbcTemplate jdbcTemplate = getWriteJdbcTemplate(db.getSn());
		
		SqlRunningBean srb = example.countSql(db);
		
		if (LogConfig.getInstance().isEnableDebug()) {
			log.info("SimpleSplitJdbcTemplate.select, the split sql:{}, params:{}",srb.getSql(),srb.getParams());
		}
		return jdbcTemplate.queryForObject(srb.getSql(), srb.getParams(), Integer.class);
	}
	
	@Override
	public <K, T> int update(K splitKey, T bean, T condition) {
		DatabaseTableVO db = obtainDbAndTb(splitKey, null,bean);
		JdbcTemplate jdbcTemplate = getWriteJdbcTemplate(db.getSn());
		SqlRunningBean srb = SqlUtil.generateUpdateSql(bean, condition, db.getPhysicalTableName());
		if (LogConfig.getInstance().isEnableDebug()) {
			log.info("SimpleSplitJdbcTemplate.doUpdate, the split sql:{}, params:{}",srb.getSql(),srb.getParams());
		}
		int updateCount = jdbcTemplate.update(srb.getSql(), srb.getParams());
		if (LogConfig.getInstance().isEnableDebug()) {
			log.info("SimpleSplitJdbcTemplate.update, updateCount:{}",updateCount);
		}
		return updateCount;
	}
	
	@Override
	public <K, T> int update(K splitKey, T bean, long id) {
		DatabaseTableVO db = obtainDbAndTb(splitKey, null,bean);
		JdbcTemplate jdbcTemplate = getWriteJdbcTemplate(db.getSn());
		SqlRunningBean srb = SqlUtil.generateUpdateSqlWithId(bean, id, db.getPhysicalTableName());
		if (LogConfig.getInstance().isEnableDebug()) {
			log.info("SimpleSplitJdbcTemplate.update, the split sql:{}, params:{}",srb.getSql(),srb.getParams());
		}
		int updateCount = jdbcTemplate.update(srb.getSql(), srb.getParams());
		if (LogConfig.getInstance().isEnableDebug()) {
			log.info("SimpleSplitJdbcTemplate.update, updateCount:{}",updateCount);
		}
		return updateCount;
	}
	
	
	
	protected <K,T> int doUpdate(K splitKey, final Class<?> clazz, UpdateOper updateOper,
			T bean, long id) {
		
		DatabaseTableVO db = obtainDbAndTb(splitKey, null,bean);
		
		JdbcTemplate jdbcTemplate = getWriteJdbcTemplate(db.getSn());
		
		SqlRunningBean srb = null;
		switch (updateOper) {
		case INSERT:
			srb = SqlUtil.generateInsertSql(bean, db.getDbPrefix(), db.getTablePrefix(), db.getDbIndex(), db.getTableIndex(),db);
			break;
		case DELETE:
			srb = SqlUtil.generateDeleteSql(id, clazz, db.getDbPrefix(), db.getTablePrefix(), db.getDbIndex(), db.getTableIndex());
			break;
		
		default:
			break;
		}
		if (LogConfig.getInstance().isEnableDebug()) {
			log.info("SimpleSplitJdbcTemplate.doUpdate, the split sql:{}, params:{}",srb.getSql(),srb.getParams());
		}
		int updateCount = jdbcTemplate.update(srb.getSql(), srb.getParams());
		if (LogConfig.getInstance().isEnableDebug()) {
			log.info("SimpleSplitJdbcTemplate.doUpdate, updateCount:{}",updateCount);
		}
		return updateCount;
		
	}
	
	protected <K,T> List<T> doSearch(K splitKey, final T bean, String name, Object valueFrom,
			Object valueTo, SearchOper searchOper) {
		return doSearch(splitKey, bean,null, name, valueFrom, valueTo, searchOper, 0, 0);
	}
	
	protected <K,T> List<T> doSearch(K splitKey, final T bean,final Class<T> clazz, String name, Object valueFrom,
			Object valueTo, SearchOper searchOper, int offset, int limit) {
		DatabaseTableVO db = obtainDbAndTb(splitKey, clazz,bean);
		// TODO 要改为从读数据库获取数据
		JdbcTemplate jdbcTemplate = super.getWriteJdbcTemplate(db.getSn());
		
		SqlRunningBean srb = null;
		switch (searchOper) {
		case NORMAL:
			srb = SqlUtil.generateSearchSql(bean, db.getPhysicalTableName(),offset,limit);
			break;

		default:
			break;
		}
		
		if (LogConfig.getInstance().isEnableDebug()) {
			log.info(
					"SimpleSplitJdbcTemplate.doSearch, the split SQL: {}, the split params: {}.",
					srb.getSql(), srb.getParams());
		}
		
		List<T> beans = jdbcTemplate.query(srb.getSql(), srb.getParams(), new RowMapper<T>() {

			@SuppressWarnings("unchecked")
			@Override
			public T mapRow(ResultSet arg0, int arg1) throws SQLException {
				if (bean != null) {
					return (T) OrmUtil.convertRow2Bean(arg0, bean.getClass());
				}else {
					return (T) OrmUtil.convertRow2Bean(arg0, clazz);
				}
			}
		});
		
		return beans;
	}
	
	
}
