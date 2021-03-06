package ivg.cn.print.service;

import ivg.cn.common.DreamResponse;
import ivg.cn.print.dto.PrintDetailDTO;

public interface PrintService {

	/** 制单 */
	DreamResponse<String> genPrintId();
	
	/** 提交打印单 */
	DreamResponse<Integer> insertPrintDetail(PrintDetailDTO printDetailDTO);
}
