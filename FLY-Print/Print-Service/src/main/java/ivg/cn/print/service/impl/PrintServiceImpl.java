package ivg.cn.print.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.fastjson.JSON;

import ivg.cn.common.DreamResponse;
import ivg.cn.common.UtilAll;
import ivg.cn.dbsplit.RowBounds;
import ivg.cn.dbsplit.core.sql.Example;
import ivg.cn.kclient.KClientProducer;
import ivg.cn.print.dao.PrintDao;
import ivg.cn.print.dao.PrintDetailDao;
import ivg.cn.print.dao.PrintDetailEpcDao;
import ivg.cn.print.dbservice.DBPrintService;
import ivg.cn.print.dto.PrintDetailDTO;
import ivg.cn.print.entity.Print;
import ivg.cn.print.entity.PrintDetail;
import ivg.cn.print.service.PrintConst;
import ivg.cn.print.service.PrintService;
import ivg.cn.print.service.iner.InerPrintService;
import ivg.cn.print.status.PrintStatus;
import ivg.cn.vesta.Id;
import ivg.cn.vesta.IdService;

@Service
@com.alibaba.dubbo.config.annotation.Service
public class PrintServiceImpl implements PrintService, InerPrintService{

	static Logger log = LoggerFactory.getLogger(PrintServiceImpl.class);
	
	@Reference(timeout=3000)
	PrintDao printDao;
	
	@Reference(timeout=3000)
	PrintDetailDao printDetailDao;
	
	@Reference(timeout=3000)
	private PrintDetailEpcDao printDetailEpcDao;
	
	@Autowired
	private IdService idService;
	
	@Reference(timeout=6000,retries=0,actives=100)
	private DBPrintService dbPrintService;
	
	@Autowired
	private KClientProducer kclientProducer;
	
	@Autowired
	private PrintTaskBussinessPool printPool;
	
	@Override
	public DreamResponse<String> genPrintId() {
		long id = idService.genId();
		Id bean = idService.expId(id);
		Date date = idService.transTime(bean.getTime());
		String dayStr = UtilAll.date2DayString(date);
		DreamResponse<String> response = DreamResponse.createOKResponse();
		
		response.setObjData("PB"+dayStr+bean.getMachine()+""+bean.getSeq());
		Map<String, Object> extData = new HashMap<>(1);
		extData.put("id", id);
		response.setExtData(extData);
		return response;
	}
	
	/**
	 * 根据打印的条码和商品的数量异步生成EPC
	 * 消息表设计：详情表Id、打印流水号、消息状态、创建时间:
	 * 发送消息，内容包括(消息Id)
	 * 消费者：收到消息后，查看是否正在生成(利用分布式锁), 若不是, 则修改状态为正在生成，同时开启生成任务、所有生成任务在一个事物里执行(可优化)，
	 * 若生成成功，则修改消息表的状态，同时释放分布式锁
	 * 
	 * 生产端，定时任务1-扫描消息表(每隔5分钟，取出近一个小时未完成)，若超过5分钟还未消费，则继续发送该消息到消息中间件,检查时超过15分钟则表明这次创建任务失败，让用户重新创建任务
	 * */
	@Override
	public DreamResponse<Integer> insertPrintDetail(PrintDetailDTO printDetailDTO) {
		// 1、先写入数据库，并将状态改为制单
		// 2、发送一条消息到消息中间件
		
		Date date = new Date();
		
		dbPrintService.insertPrint(date, printDetailDTO);
		
		Map<String, Object> param = new HashMap<>(2);
		param.put("id", printDetailDTO.getPrintId());
		param.put("date",date);
		kclientProducer.send2Topic(PrintConst.KafkaPrintEpcTopic, PrintConst.KafkaPrintEpcTopicKey, JSON.toJSONString(param));
		return DreamResponse.createOKResponse();
	}
	
	@Override
	public void receivePrintEpcMessage(Date date, long printId) {
		// 1、查主单状态是否为已完成，若是，则不用处理了
		// 2、若主单不是已完成，则遍历明细表，查看每个明细表的状态是否有未生成的，若有未生成的则获取锁，同时继续查看是否已是未完成，若是则锁住该EPC
		
		Print print = printDao.selectOne(date, printId, Print.class);
		if (print == null || PrintStatus.GeneratFinshed.getValue() == print.getStatus()) {
			return;
		}
		
		PrintDetail bean = new PrintDetail();
		bean.setPrintId(printId);
		List<PrintDetail> deList = printDetailDao.select(date, bean);
		if (deList == null || deList.size() == 0) {
			return;
		}
		
		int latchCount = 0;
		for (PrintDetail printDetail : deList) {
			if (PrintStatus.BuildBill.getValue() == printDetail.getStatus()) {
				// 还未打印的
				latchCount++;
			}
		}
		// 如果打印明细表都打印完成，则更新主表状态为打印完成
		if (latchCount == 0) {
			Print pt = new Print();
			pt.setStatus(PrintStatus.GeneratFinshed.getValue());
			printDao.updateWithId(date, pt, printId);
			return;
		}
		
		CountDownLatch latch = new CountDownLatch(latchCount);
		boolean haveFailed = false;
		for (PrintDetail printDetail : deList) {
			if (PrintStatus.BuildBill.getValue() == printDetail.getStatus()) {
				// 还未打印的
				Future<?> rFuture = printPool.submitTask(new Runnable() {
					
					@Override
					public void run() {
						try {
							dbPrintService.processPrintTask(printDetail);
						} finally {
							
						}
					}
				});
				try {
					rFuture.get();
				} catch (InterruptedException | ExecutionException e) {
					haveFailed = true;
					e.printStackTrace();
				}finally {
					latch.countDown();
				}
			}
		}
		try {
			if (latch.await(latchCount*2000,TimeUnit.MILLISECONDS) && !haveFailed) {
				// 如果没有执行失败的，就更新数据
				Print pt = new Print();
				pt.setStatus(PrintStatus.GeneratFinshed.getValue());
				printDao.updateWithId(date, pt, printId);
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public void scanNoPrintBill() {
		
		Example example = new Example(Print.class);
		long begin = (long) (System.currentTimeMillis() - 3600*1000*48*2*3);
		Date beDate = new Date(begin);
		Date now = new Date();
		
		example.andBetween("createDate", beDate, now);
		example.andEqual("status", PrintStatus.BuildBill.getValue());
		
		Integer records = printDao.count(beDate, example);
		if (records == null || records == 0) {
			return;
		}
		int pageSize = 20;
		int pages = (records%pageSize == 0)?(records/pageSize):((records/pageSize) + 1);
		for (int i = 0; i < pages; i++) {
			scanNoPrintBillWithPages(i, pageSize);
		}
	}
	
	private void scanNoPrintBillWithPages(int page, int pageSize) {
		
		Example example = new Example(Print.class);
		long begin = (long) (System.currentTimeMillis() - 3600*1000*48*2*3);
		Date beDate = new Date(begin);
		Date now = new Date();
		
		example.andBetween("createDate", beDate, now);
		example.andEqual("status", PrintStatus.BuildBill.getValue());
		example.addRowBounds(new RowBounds(page, pageSize));

		List<Print> details = printDao.select(beDate, example);
		
		if (details == null || details.size()==0) {
			return;
		}
		
		// 查其详表是否均打印完
		for (Print print : details) {
			PrintDetail bean = new PrintDetail();
			bean.setPrintId(print.getId());
			bean.setStatus( PrintStatus.BuildBill.getValue());
			List<PrintDetail> deList = printDetailDao.select(beDate, bean);
			if (deList == null || deList.size()==0) {
				Print pt = new Print();
				pt.setStatus(PrintStatus.GeneratFinshed.getValue());
				printDao.updateWithId(print.getCreateDate(), pt, print.getId());
				continue;
			}
			submitPrintTask(deList, print);
		}
	}
	
	private void submitPrintTask(List<PrintDetail> deList, Print print) {
		CountDownLatch latch = new CountDownLatch(deList.size());
		AtomicBoolean haveFailed = new AtomicBoolean(false);
		for (PrintDetail printDetail : deList) {
			if (PrintStatus.BuildBill.getValue() == printDetail.getStatus()) {
				// 还未打印的
				
				try {
					Future<?> ret = printPool.submitTask(new Runnable() {
						
						@Override
						public void run() {
							try {
								dbPrintService.processPrintTask(printDetail);
							} finally {
							}
						}
					});
					ret.get();
				} catch (Throwable e) {
					haveFailed.set(true);
					e.printStackTrace();

				}finally {
					latch.countDown();
				}
			}
		}
		try {
			if (latch.await(deList.size()*2000,TimeUnit.MILLISECONDS) && !haveFailed.get()) {
				// 如果没有执行失败的，就更新数据
				log.info("Has gen finshed, printId={}",print.getId());
				Print pt = new Print();
				pt.setStatus(PrintStatus.GeneratFinshed.getValue());
				printDao.updateWithId(print.getCreateDate(), pt, print.getId());
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
