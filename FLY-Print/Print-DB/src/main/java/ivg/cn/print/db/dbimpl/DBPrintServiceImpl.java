package ivg.cn.print.db.dbimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ivg.cn.print.dao.PrintDao;
import ivg.cn.print.dao.PrintDetailDao;
import ivg.cn.print.dao.PrintDetailEpcDao;
import ivg.cn.print.db.cache.PrintDetailStatusCache;
import ivg.cn.print.db.cache.PrintDetailStatusRedisCache;
import ivg.cn.print.dbservice.DBPrintService;
import ivg.cn.print.dto.PrintDetailDTO;
import ivg.cn.print.dto.PrintDetailDTO.InerDetail;
import ivg.cn.print.entity.Print;
import ivg.cn.print.entity.PrintDetail;
import ivg.cn.print.entity.PrintDetailEpc;
import ivg.cn.print.status.PrintStatus;
import ivg.cn.tools.dislock.DisLock;
import ivg.cn.tools.dislock.DisLockResult;
import ivg.cn.vesta.IdService;

@Service
@com.alibaba.dubbo.config.annotation.Service(timeout=5000,retries=0)
public class DBPrintServiceImpl implements DBPrintService{

	static Logger log = LoggerFactory.getLogger(DBPrintServiceImpl.class);
	
	@Autowired
	private PrintDao printDao;
	
	@Autowired
	private PrintDetailDao printDetailDao;
	
	@Autowired
	private PrintDetailEpcDao printDetailEpcDao;
	
	@Autowired
	private IdService idService;
	
	@Autowired
	private DisLock disLock;
	
	@Autowired
	private PrintDetailStatusRedisCache redisCache;
	
	@Transactional
	@Override
	public void insertPrint(Date date,PrintDetailDTO printDetailDTO) {
		// 1、插入print主表,状态均为未打印
		// 2、插入打印明细表
		
		Print print = new Print();
		BeanUtils.copyProperties(printDetailDTO, print);
		print.setId(printDetailDTO.getPrintId());
		print.setCreateDate(date);
		print.setUpdateDate(date);
		print.setCreater(printDetailDTO.getOperatorId());
		print.setUpdater(printDetailDTO.getOperatorId());
		print.setStatus(PrintStatus.BuildBill.getValue());
		printDao.insert(date, print);
		
		List<PrintDetail> pds = new ArrayList<PrintDetail>( printDetailDTO.getDetailItems().size());
		for(InerDetail de : printDetailDTO.getDetailItems()){
			PrintDetail pd = new PrintDetail();
			BeanUtils.copyProperties(print, pd);
			BeanUtils.copyProperties(de, pd);
			pd.setId(idService.genId());
			pd.setPrintId(print.getId());
			pds.add(pd);
		}
		
		printDetailDao.batchInsert(date, pds);
	}
	
	@Transactional
	@Override
	public void processPrintTask(PrintDetail printDetail) {
		// 1、先尝试获取分布式锁,若获取成功，则继续
		
		DisLockResult disLockResult = null;
		boolean noexception = false;
		String resId = String.valueOf(printDetail.getId());
		if (PrintDetailStatusCache.getInstance().hasFinshed(resId)) {
			return;
		}
		if (redisCache.hasFinshed(resId)) {
			return;
		}
		Date date = new Date();
		try {
			disLockResult = disLock.tryLock(resId, 0);
			if (disLockResult.isSuccess()) {
				if (redisCache.hasFinshed(resId)) {
					return;
				}
				PrintDetail detail = printDetailDao.selectOne(printDetail.getCreateDate(), printDetail.getId(), PrintDetail.class);
				if (detail == null || PrintStatus.BuildBill.getValue() != printDetail.getStatus()) {
					return;
				}
//				log.info("printId={}, printQty={}",detail.getPrintId(),detail.getPrintQty());
				// 生成打印EPC
				List<PrintDetailEpc> epList = new ArrayList<>(printDetail.getPrintQty());
				for(int i=0; i<printDetail.getPrintQty(); i++){
					PrintDetailEpc item = new PrintDetailEpc();
					BeanUtils.copyProperties(printDetail, item);
					long id = idService.genId();
					item.setId(id);
					item.setEpc(epc(id));
					item.setCreateDate(date);
					item.setUpdateDate(date);
					item.setDetailCreateDate(printDetail.getCreateDate());
					epList.add(item);
				}
				// 批量插入EPC 
				printDetailEpcDao.batchInsert(printDetail.getCreateDate(), epList);
				
				// 更新 明细表状态
				PrintDetail bean = new PrintDetail();
				bean.setStatus(PrintStatus.GeneratFinshed.getValue());
				printDetailDao.updateWithId(printDetail.getCreateDate(), bean, printDetail.getId());
				noexception = true;
			}
		} finally {
			if (disLockResult != null && disLockResult.isSuccess()) {
				if (noexception) {
					PrintDetailStatusCache.getInstance().putFinshed(resId);
					redisCache.putFinshed(resId);
				}
				disLock.release(disLockResult, noexception);
			}
		}
		
	}
	
	public String epc(long id) {
		StringBuilder sb = new StringBuilder(String.valueOf(id));
		for(int i=sb.length(); i<24; i++){
			sb.append("0");
		}
		return sb.toString();
	}

}
