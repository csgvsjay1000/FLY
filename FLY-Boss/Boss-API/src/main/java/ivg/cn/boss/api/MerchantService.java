package ivg.cn.boss.api;

import ivg.cn.boss.dto.InsertMerchantDTO;
import ivg.cn.common.DreamResponse;

public interface MerchantService {

	/** 添加商户 */
	DreamResponse<Integer> insertMerchant(InsertMerchantDTO merchantDTO);
	
	/** 删除商户 */
	DreamResponse<Integer> deleteMerchant(long id);
	
}
