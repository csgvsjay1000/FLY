package ivg.cn.boss.db.provider;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ivg.cn.boss.api.MerchantService;
import ivg.cn.boss.db.dao.MerchantDao;
import ivg.cn.boss.db.dao.UserDao;
import ivg.cn.boss.dto.InsertMerchantDTO;
import ivg.cn.boss.entity.BossMerchant;
import ivg.cn.boss.entity.BossUser;
import ivg.cn.common.DreamResponse;

@com.alibaba.dubbo.config.annotation.Service
@Service("merchantService")
public class MerchantProvider implements MerchantService{

	@Autowired
	private MerchantDao merchantDao;
	
	@Autowired
	private UserDao userDao;

	@Transactional
	@Override
	public DreamResponse<Integer> insertMerchant(InsertMerchantDTO merchantDTO) {
		// 1、添加商户
		// 2、添加商户对应的用户
		
		Date date = new Date();
		
		BossMerchant merchant = new BossMerchant();
		BeanUtils.copyProperties(merchantDTO, merchant);
		merchant.setId(System.currentTimeMillis());
		merchant.setCreateDate(date);
		merchant.setUpdateDate(date);
		merchant.setCreater(1L); // 商户添加人只有超级管理员
		merchant.setUpdater(1L);
		int updateCount = merchantDao.insert(merchant.getId(), merchant);
		
		BossUser bossUser = new BossUser();
		bossUser.setUsername(merchantDTO.getUsername());
		bossUser.setPassword(merchantDTO.getPassword());
		bossUser.setCreateDate(date);
		bossUser.setUpdateDate(date);
		bossUser.setCreater(1L); // 商户添加人只有超级管理员
		bossUser.setUpdater(1L);
		bossUser.setId(System.currentTimeMillis());
		bossUser.setMerchantId(merchant.getId());
		userDao.insert(bossUser.getId(), bossUser);
		
		DreamResponse<Integer> response = DreamResponse.createOKResponse();
		response.setObjData(updateCount);
		return response;
	}
	
	@Transactional
	@Override
	public DreamResponse<Integer> deleteMerchant(long id) {
		int updateCount = merchantDao.delete(id, id, BossMerchant.class);
		DreamResponse<Integer> response = DreamResponse.createOKResponse();
		response.setObjData(updateCount);
		return response;
	}

}
