package ivg.cn.replenish.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;

import ivg.cn.common.DreamResponse;
import ivg.cn.print.dao.PrintDetailEpcDao;
import ivg.cn.print.entity.PrintDetailEpc;
import ivg.cn.replenish.entity.ValidateEpcDTO;
import ivg.cn.replenish.service.ReplenishService;

@Service
@com.alibaba.dubbo.config.annotation.Service(version="1")
public class ReplenishServiceImpl implements ReplenishService{

	@Reference(timeout=3000)
	private PrintDetailEpcDao printDetailEpcDao;
	
	@Override
	public DreamResponse<PrintDetailEpc> select(ValidateEpcDTO epcDto) {
		
		PrintDetailEpc record = new PrintDetailEpc();
		record.setEpc(epcDto.getEpc());
		Date date = new Date();
		PrintDetailEpc detailEpc = printDetailEpcDao.selectOne(date, record);
		DreamResponse<PrintDetailEpc> response = DreamResponse.createOKResponse();
		response.setObjData(detailEpc);
		return response;
	}

}
