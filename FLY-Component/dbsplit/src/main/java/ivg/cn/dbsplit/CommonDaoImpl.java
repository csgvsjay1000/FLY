package ivg.cn.dbsplit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ivg.cn.dbsplit.core.SimpleSplitJdbcTemplate;
import ivg.cn.dbsplit.core.sql.Example;

public class CommonDaoImpl implements CommonDao{

	@Autowired
	protected SimpleSplitJdbcTemplate splitJdbcTemplate;
	
	@Override
	public <K, T> int insert(K splitKey, T bean) {
		return splitJdbcTemplate.insert(splitKey, bean);
	}

	@Override
	public <K, T> int delete(K splitKey, long id, Class<T> clazz) {
		return splitJdbcTemplate.delete(splitKey, id, clazz);
	}
	
	@Override
	public <K, T> List<T> select(K splitKey, T bean) {
		return splitJdbcTemplate.select(splitKey, bean);
	}
	
	@Override
	public <K, T> List<T> selectAll(K splitKey, Class<T> clazz) {
		return splitJdbcTemplate.selectAll(splitKey, clazz);
	}

	@Override
	public <K, T> List<T> selectPages(K splitKey, T bean, int offset, int limit) {
		return splitJdbcTemplate.selectPages(splitKey, bean, offset, limit);
	}
	
	@Override
	public <K, T> List<T> selectByRowBounds(K splitKey, T bean, RowBounds rowBounds) {
		return splitJdbcTemplate.selectPages(splitKey, bean, rowBounds.getOffset(), rowBounds.getLimit());
	}

	@Override
	public <K, T> T selectOne(K splitKey, long id, Class<T> clazz) {
		return splitJdbcTemplate.selectOne(splitKey, id, clazz);
	}
	
	@Override
	public <K, T> T selectOne(K splitKey, T bean) {
		return splitJdbcTemplate.selectOne(splitKey, bean);
	}
	
	@Override
	public <K, T> int batchInsert(K splitKey, List<T> beans) {
		return splitJdbcTemplate.batchInsert(splitKey, beans);
	}
	
	@Override
	public <K, T> int update(K splitKey, T bean, T condition) {
		return splitJdbcTemplate.update(splitKey, bean, condition);
	}
	
	@Override
	public <K, T> int updateWithId(K splitKey, T bean, long id) {
		return splitJdbcTemplate.update(splitKey, bean, id);
	}
	
	@Override
	public <K, T> List<T> select(K splitKey, Example example) {
		return splitJdbcTemplate.select(splitKey, example);
	}
	
	@Override
	public <K, T> List<T> select(K splitKey, Example example, RowBounds rowBounds) {
		return splitJdbcTemplate.select(splitKey, example, rowBounds);
	}
	
	@Override
	public <K> Integer count(K splitKey, Example example) {
		return splitJdbcTemplate.count(splitKey, example);
	}
	
}
