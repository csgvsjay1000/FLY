package ivg.cn.print.dbservice;

import java.util.Date;

import ivg.cn.print.dto.PrintDetailDTO;
import ivg.cn.print.entity.PrintDetail;

public interface DBPrintService {

	/**
	 * 添加一条打印需求到print表
	 * 
	 * @return 返回printId
	 * */
	void insertPrint(Date date,PrintDetailDTO printDetailDTO);
	
	/**
	 * 处理一个生成EPC任务
	 * */
	void processPrintTask(PrintDetail printDetail);
	
}
