package ivg.cn.monitor.zk.db.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ivg.cn.monitor.zk.db.ZKItemDao;
import ivg.cn.monitor.zk.db.service.ZkDBService;

@Service
public class ZkDBServiceImpl implements ZkDBService{

	@Autowired
	private ZKItemDao zkItemDao;
	
}
