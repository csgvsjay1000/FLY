package ivg.cn.merchant.db.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;

import ivg.cn.dbsplit.core.SimpleSplitJdbcTemplate;
import ivg.cn.merchant.db.dao.CommonDao;

public class CommonDaoImpl implements CommonDao{

	@Autowired
	protected SimpleSplitJdbcTemplate splitJdbcTemplate;
	
	@Override
	public <K, T> int insert(K splitKey, T bean) {
		
		return splitJdbcTemplate.insert(splitKey, bean);
	}
	
	

}
