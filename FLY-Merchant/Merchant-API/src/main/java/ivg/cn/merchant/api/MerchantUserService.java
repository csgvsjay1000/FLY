package ivg.cn.merchant.api;

import ivg.cn.common.DreamResponse;
import ivg.cn.merchant.entity.MerchantUser;

public interface MerchantUserService {

	DreamResponse<Integer> insert(MerchantUser merchantUser);
	
}
