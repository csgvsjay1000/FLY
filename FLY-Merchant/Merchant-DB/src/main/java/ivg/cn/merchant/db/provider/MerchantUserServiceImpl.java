package ivg.cn.merchant.db.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ivg.cn.common.DreamResponse;
import ivg.cn.merchant.api.MerchantUserService;
import ivg.cn.merchant.db.dao.MerchantUserDao;
import ivg.cn.merchant.entity.MerchantUser;

@Service("merchantUserService")
@com.alibaba.dubbo.config.annotation.Service(export=true,interfaceClass=MerchantUserService.class)
public class MerchantUserServiceImpl implements MerchantUserService{

	@Autowired
	private MerchantUserDao merchantUserDao;
	
	@Override
	public DreamResponse<Integer> insert(MerchantUser merchantUser) {
		int updateCount = merchantUserDao.insert(merchantUser.getId(), merchantUser);
		DreamResponse<Integer> response = DreamResponse.createOKResponse();
		response.setObjData(updateCount);
		return response;
	}

}
