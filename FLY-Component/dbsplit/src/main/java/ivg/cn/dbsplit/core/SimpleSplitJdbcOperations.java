package ivg.cn.dbsplit.core;

import java.util.List;

import ivg.cn.dbsplit.RowBounds;
import ivg.cn.dbsplit.core.sql.Example;

public interface SimpleSplitJdbcOperations extends SplitJdbcOperations{

	<K,T> int insert(K splitKey, T bean);
	
	<K,T> int delete(K splitKey, long id, Class<T> clazz);
	
	<K,T> List<T> select(K splitKey, T bean);
	
	<K,T> List<T> selectAll(K splitKey, Class<T> clazz);
	
	<K,T> List<T> selectPages(K splitKey, T bean, int offset, int limit);
	
	<K,T> T selectOne(K splitKey, long id, Class<T> clazz);
	
	<K,T> T selectOne(K splitKey, T bean);
	
	/**  批量插入, 必须保证批量插入只插入同一库中 */
	<K,T> int batchInsert(K splitKey, List<T> beans);
	
	/**
	 * 更新
	 * @param bean 要更新的的数据
	 * @param condition 更新的条件
	 * */
	<K,T> int update(K splitKey, T bean, T condition);
	
	<K,T> int update(K splitKey, T bean, long id);
	
	<K,T> List<T> select(K splitKey, Example example);
	
	<K, T> List<T> select(K splitKey, Example example, RowBounds rowBounds);
	
	/** 获取记录数 */
	<K> Integer count(K splitKey, Example example);
	
}
