package ivg.cn.commodity.api;

import ivg.cn.commodity.entity.CommodityAssert;
import ivg.cn.common.DreamResponse;

public interface CommodityAssertService {
	
	DreamResponse<Integer> insert(CommodityAssert commodityAssert);

}
