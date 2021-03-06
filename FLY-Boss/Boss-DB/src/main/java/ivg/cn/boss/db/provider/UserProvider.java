package ivg.cn.boss.db.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ivg.cn.boss.api.UserService;
import ivg.cn.boss.db.dao.UserDao;
import ivg.cn.boss.entity.BossUser;
import ivg.cn.common.DreamResponse;

@Service
@com.alibaba.dubbo.config.annotation.Service
public class UserProvider implements UserService{

	@Autowired
	private UserDao userDao;
	
	@Transactional
	@Override
	public DreamResponse<Integer> delete(long id) {
		int updateCount = userDao.delete(id, id, BossUser.class);
		DreamResponse<Integer> response = DreamResponse.createOKResponse();
		response.setObjData(updateCount);
		return response;
	}

}
