package ivg.cn.dbsplit.core;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.RowMapper;

public interface SplitJdbcOperations {

	<T,K> T execute(K splitKey, ConnectionCallback<T> action) throws DataAccessException;
	
	<K,T> List<T> query(K splitKey, String sql, Object[] args, RowMapper<T> rowMapper);
	
}
