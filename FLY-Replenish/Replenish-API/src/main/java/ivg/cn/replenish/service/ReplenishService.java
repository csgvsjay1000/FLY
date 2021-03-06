package ivg.cn.replenish.service;

import ivg.cn.common.DreamResponse;
import ivg.cn.print.entity.PrintDetailEpc;
import ivg.cn.replenish.entity.ValidateEpcDTO;

/**
 * 补货服务
 * */
public interface ReplenishService {

	/**
	 * 前端生成补货单时，校验EPC接口
	 * */
	DreamResponse<PrintDetailEpc> select(ValidateEpcDTO epcDto);
	
}
