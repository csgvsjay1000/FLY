package ivg.cn.print.service.iner;

import java.util.Date;

public interface InerPrintService {

	/**
	 * 收到一条打印EPC需求信息
	 * @param date 打印时间
	 * @param printId打印信息ID
	 * */
	void receivePrintEpcMessage(Date date, long printId);
	
	/**
	 * 定期扫描未开始打印的明细单，并更新主单信息
	 * */
	void scanNoPrintBill();
	
}
