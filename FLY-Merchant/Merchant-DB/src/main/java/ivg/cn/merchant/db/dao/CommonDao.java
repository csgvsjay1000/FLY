package ivg.cn.merchant.db.dao;

public interface CommonDao {

	<K,T> int insert(K splitKey, T bean);
	
}
