package ivg.cn.monitor.zk;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ivg.cn.monitor.zk.db.ZKItemDao;
import ivg.cn.monitor.zk.db.ZkInstMonitorDao;
import ivg.cn.monitor.zk.entity.ZkInstMonitor;
import ivg.cn.monitor.zk.entity.ZkItem;
import ivg.cn.monitor.zk.nio.ZKConnection;
import ivg.cn.monitor.zk.nio.ZKReactor;
import ivg.cn.monitor.zk.nio.ZKSendCmdResult;
import ivg.cn.monitor.zk.parser.SrvrParser;
import ivg.cn.monitor.zk.util.ExecutorUtils;
import ivg.cn.vesta.IdService;

public class MonitorZkController {

	static Logger logger = LoggerFactory.getLogger(MonitorZkController.class);
	
	ZKItemDao zkItemDao;
	ZkInstMonitorDao instMonitorDao;
	IdService idService;
	
	ScheduledExecutorService scanZkItemService = ExecutorUtils.namedSingleScheduleExecutor("ScanZkItemService"); 
	ExecutorService checkZkItemService = ExecutorUtils.traceFixedThreadPool("CheckZkItemService", 20);
	
	ZKReactor zkReactor; 
	
	public MonitorZkController() {
		zkReactor = new ZKReactor();
	}
	
	public void start() {
		logger.info("start ...");
		
		zkReactor.start();
		
		scanZkItemService.scheduleAtFixedRate(scanZkItems(), 10, 10, TimeUnit.SECONDS);
	}
	
	private Runnable scanZkItems() {
		
		return new Runnable() {
			
			@Override
			public void run() {
				List<ZkItem> zkItems = zkItemDao.selectAll(null, ZkItem.class);
				if (zkItems == null) {
					return;
				}
				for (ZkItem zkItem : zkItems) {
					checkZkItemService.submit(checkZkItemStatus(zkItem));
				}
			}
		};
	}
	
	public Runnable checkZkItemStatus(ZkItem zkItem) {
		return new Runnable() {
			
			@Override
			public void run() {
				
				ZKConnection conn = new ZKConnection(zkReactor,zkItem.getZkIp(),zkItem.getZkPort());
				ZKSendCmdResult result = conn.sendCmd(ZKFourCmd.SRVR);
				if (result != null && result.isSuccess()) {
					ZkInstMonitor instMonitor = SrvrParser.parse(zkItem, result.getResult());
					instMonitor.setId(idService.genId());
					instMonitorDao.insert(null, instMonitor);
				}
			}
		};
	}
	
	public void stop() {
		logger.info("stop ...");
	}
	
	public void setZkItemDao(ZKItemDao zkItemDao) {
		this.zkItemDao = zkItemDao;
	}
	
	public void setInstMonitorDao(ZkInstMonitorDao instMonitorDao) {
		this.instMonitorDao = instMonitorDao;
	}
	
	public void setIdService(IdService idService) {
		this.idService = idService;
	}
}
