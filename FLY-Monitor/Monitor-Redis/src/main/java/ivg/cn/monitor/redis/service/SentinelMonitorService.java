package ivg.cn.monitor.redis.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import ivg.cn.monitor.redis.GlobalCache;
import ivg.cn.monitor.redis.MonitorRedisController;
import ivg.cn.monitor.redis.RedisBean;
import ivg.cn.monitor.redis.RedisChannelProcessor;
import ivg.cn.monitor.redis.RedisDbConst.ItemStatus;
import ivg.cn.monitor.redis.RedisDbConst.MasterStatus;
import ivg.cn.monitor.redis.SentinelJedisCache;
import ivg.cn.monitor.redis.db.MasterItemMonitorDao;
import ivg.cn.monitor.redis.db.SentinelItemDao;
import ivg.cn.monitor.redis.db.SentinelItemMonitorDao;
import ivg.cn.monitor.redis.db.impl.MasterItemMonitorDaoImpl;
import ivg.cn.monitor.redis.db.impl.SentinelItemDaoImpl;
import ivg.cn.monitor.redis.db.impl.SentinelItemMonitorDaoImpl;
import ivg.cn.monitor.redis.entity.MasterItemMonitor;
import ivg.cn.monitor.redis.entity.SentinelItem;
import ivg.cn.monitor.redis.entity.SentinelItemMonitor;
import ivg.cn.monitor.redis.parser.SentinelMasterParser;
import ivg.cn.monitor.redis.process.RedisNodeProcessor;
import ivg.cn.monitor.redis.process.SentinelProcessor;
import ivg.cn.monitor.redis.process.SwitchMasterProcessor;
import ivg.cn.monitor.redis.util.ExecutorUtils;
import ivg.cn.monitor.redis.util.SpringBeanUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

public class SentinelMonitorService implements Runnable{

	ScheduledExecutorService scanSentinelItemService = null;
	ConcurrentMap<RedisBean, JedisPool> jedisPoolTable = new ConcurrentHashMap<RedisBean, JedisPool>();
	
	ConcurrentMap<String /**channel*/, RedisChannelProcessor> channelProcessTable = 
			new ConcurrentHashMap<String, RedisChannelProcessor>();
	
	ExecutorService redisInstMonitorService = ExecutorUtils.traceFixedThreadPool("SentinelMonitorService", 20);
	ExecutorService sentinelToDBService = ExecutorUtils.traceFixedThreadPool("SentinelToDBService", 20);
	
	SentinelItemDao sentinelItemDao;
	
	SentinelItemMonitorDao sentinelItemMonitorDao;
	MasterItemMonitorDao masterItemMonitorDao;
	
	final MonitorRedisController monitorRedisController;
	
	SentinelJedisCache sentinelJedisCache;
	String[] channels = null;
	
	public SentinelMonitorService(MonitorRedisController monitorRedisController) {
		sentinelItemDao = SpringBeanUtil.getBean(SentinelItemDaoImpl.class);
		sentinelItemMonitorDao = SpringBeanUtil.getBean(SentinelItemMonitorDaoImpl.class);
		masterItemMonitorDao = SpringBeanUtil.getBean(MasterItemMonitorDaoImpl.class);
		this.monitorRedisController = monitorRedisController;
		
		sentinelJedisCache = new SentinelJedisCache();

		registerChannelProcessor();
	}
	
	public void registerChannelProcessor() {
		
		channelProcessTable.putIfAbsent("switch-master", new SwitchMasterProcessor());
		channelProcessTable.putIfAbsent("+sentinel", new SentinelProcessor());
		
		RedisNodeProcessor redisNodeProcessor = new RedisNodeProcessor();
		channelProcessTable.putIfAbsent("+slave", redisNodeProcessor);  // 添加对新增从节点监听
		channelProcessTable.putIfAbsent("+sdown", redisNodeProcessor);  // 添加对某个从节点下线监听
		channelProcessTable.putIfAbsent("-sdown", redisNodeProcessor);  // 撤销某个从节点下线，即某个从节点恢复上线
		
		channels = new String[channelProcessTable.keySet().size()];
		channelProcessTable.keySet().toArray(channels);
		
	}
	
	public void start() {
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		restartScheduleTask();
	}
	
	public void restartScheduleTask() {
		
		if (scanSentinelItemService != null) {
			scanSentinelItemService.shutdown();
		}
		
		scanSentinelItemService = ExecutorUtils.namedSingleScheduleExecutor("ScanSentinelItemService");
		ScheduledFuture<?> future = scanSentinelItemService.scheduleAtFixedRate(scanSentinelItems(), GlobalCache.TimeIntervel, GlobalCache.TimeIntervel, TimeUnit.MILLISECONDS);
		try {
			future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			restartScheduleTask();
		}
	}

	private Runnable scanSentinelItems() {
	
		return new Runnable() {
			
			@Override
			public void run() {
				runTask();
			}
		};
	}
	
	private void runTask() {
		List<SentinelItem> zkItems = sentinelItemDao.selectAll(null, SentinelItem.class);
		if (zkItems == null) {
			return;
		}
		for (SentinelItem zkItem : zkItems) {
			RedisBean redisBean = new RedisBean();
			redisBean.setIp(zkItem.getSentinelIp());
			redisBean.setPort(zkItem.getSentinelPort());
			
			if (!jedisPoolTable.containsKey(redisBean)) {
				synchronized (this) {
					if (!jedisPoolTable.containsKey(redisBean)) {
						GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
						poolConfig.setMaxTotal(1);
						JedisPool jedisPool = new JedisPool(poolConfig, redisBean.getIp(),redisBean.getPort(), 3000);
						jedisPoolTable.putIfAbsent(redisBean, jedisPool);
					}
				}
			}
			JedisPool jedisPool = jedisPoolTable.get(redisBean);
			if (jedisPool != null) {
				redisInstMonitorService.submit(insertSentinelMonitor(zkItem,redisBean,jedisPool));
			}
		}
	}
	
	private Runnable insertSentinelMonitor(SentinelItem sentinelItem,RedisBean redisBean, JedisPool jedisPool) {
		return new Runnable() {
			
			@Override
			public void run() {
				Jedis jedis = null;
				long cost = 0;
				try {
					jedis = jedisPool.getResource();
					long begin = System.currentTimeMillis();
					jedis.ping();
					cost = System.currentTimeMillis()-begin;
					
					// 保存集群状态
					List<Map<String, String>> masters = jedis.sentinelMasters();
					redisInstMonitorService.submit(processSentinelMaster(masters));
					
					// 保存sentinel状态
					sentinelToDBService.submit(saveToDB(redisBean,cost));
					
					// 异步订阅主题
					sentinelJedisCache.subscribe(jedis, new InerJedisPubSub(), channels);
					
					// 维护SentinelItem状态
					if (ItemStatus.Offline == sentinelItem.getStatus()) {
						sentinelToDBService.submit(updateSentinelItem(sentinelItem, MasterStatus.Online,ItemStatus.Online));
					}
				} catch (JedisConnectionException e) {
//					e.printStackTrace();
					if (ItemStatus.Online == sentinelItem.getStatus()) {
						sentinelToDBService.submit(updateSentinelItem(sentinelItem, MasterStatus.Offline,ItemStatus.Offline));
					}
					
				}catch (JedisException e) {
//					e.printStackTrace();
					
				}finally {
					if (jedis != null) {
						jedis.close();
					}
				}
				
			}
		};
	}
	
	private Runnable processSentinelMaster(List<Map<String, String>> masters) {
		return new Runnable() {
			
			@Override
			public void run() {
				if (masters == null || masters.size() == 0) {
					return;
				}
				for (Map<String, String> map : masters) {
					MasterItemMonitor monitor = SentinelMasterParser.parse(map);
					monitor.setId(monitorRedisController.getIdService().genId());
					masterItemMonitorDao.insert(null, monitor);
				}
			}
		};
	}
	
	private Runnable saveToDB(RedisBean redisBean, long cost) {
		return new Runnable() {
			
			@Override
			public void run() {
				SentinelItemMonitor itemMonitor = new SentinelItemMonitor();
				itemMonitor.setCreateDate(new Date());
				itemMonitor.setId(monitorRedisController.getIdService().genId());
				itemMonitor.setSentinelIp(redisBean.getIp());
				itemMonitor.setSentinelPort(redisBean.getPort());
				itemMonitor.setPingCost((int)cost);
				sentinelItemMonitorDao.insert(null, itemMonitor);
			}
		};
		
	}
	
	private Runnable updateSentinelItem(SentinelItem sentinelItem,String statusCh, int status) {
		return new Runnable() {
			
			@Override
			public void run() {
				SentinelItem update = new SentinelItem();
				update.setStatusCh(statusCh);
				update.setStatus(status);
				update.setStatusUpdate(new Date());
				sentinelItemDao.updateWithId(null, update, sentinelItem.getId());
			}
		};
		
	}
	
	class InerJedisPubSub extends JedisPubSub{
		
		@Override
		public void onMessage(String channel, String message) {
			
			RedisChannelProcessor processor = channelProcessTable.get(channel);
			if (processor != null) {
				processor.process(channel, message);
			}
		}
		
	}
	
}
