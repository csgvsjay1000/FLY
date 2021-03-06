package ivg.cn.monitor.redis;

import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import ivg.cn.monitor.redis.db.RedisInstMonitorDao;
import ivg.cn.monitor.redis.db.RedisItemDao;
import ivg.cn.monitor.redis.entity.RedisInstMonitor;
import ivg.cn.monitor.redis.entity.RedisItem;
import ivg.cn.monitor.redis.parser.InfoParser;
import ivg.cn.monitor.redis.service.SentinelMonitorService;
import ivg.cn.monitor.redis.service.SlowlogService;
import ivg.cn.monitor.redis.util.ExecutorUtils;
import ivg.cn.vesta.IdService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

public class MonitorRedisController implements Runnable{

	IdService idService;
	RedisItemDao redisItemDao;
	RedisInstMonitorDao redisInstMonitorDao;
	
	ScheduledExecutorService scanRedisItemService = null;
	ConcurrentMap<RedisBean, JedisPool> jedisPoolTable = new ConcurrentHashMap<RedisBean, JedisPool>();
	ExecutorService redisInstMonitorService = ExecutorUtils.traceFixedThreadPool("RedisInstMonitorService", 20);
	
	SlowlogService slowlogService = null;
	SentinelMonitorService sentinelMonitorService = null;
	
	public MonitorRedisController() {
		slowlogService = new SlowlogService(this);
		sentinelMonitorService = new SentinelMonitorService(this);
	}
	
	public void start() {
		
		new Thread(this).start();
		sentinelMonitorService.start();
	}
	
	@Override
	public void run() {
		restartScheduleTask();
	}
	
	public void restartScheduleTask() {
		
		if (scanRedisItemService != null) {
			scanRedisItemService.shutdown();
		}
		
		scanRedisItemService = ExecutorUtils.namedSingleScheduleExecutor("ScanRedisItemService");
		ScheduledFuture<?> future = scanRedisItemService.scheduleAtFixedRate(scanZkItems(), GlobalCache.TimeIntervel, GlobalCache.TimeIntervel, TimeUnit.MILLISECONDS);
		try {
			future.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			restartScheduleTask();
		}
	}
	
	public TimerTask scanZkItemsTimeTask() {
		return new TimerTask() {
			
			@Override
			public void run() {
				runTask();
			}
		};
	}
	
	private void runTask() {
		List<RedisItem> zkItems = redisItemDao.selectAll(null, RedisItem.class);
		if (zkItems == null) {
			return;
		}
		for (RedisItem zkItem : zkItems) {
			RedisBean redisBean = new RedisBean();
			redisBean.setIp(zkItem.getRedisIp());
			redisBean.setPort(zkItem.getRedisPort());
			
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
				redisInstMonitorService.submit(insertRedisInst(zkItem,redisBean,jedisPool));
				slowlogService.submitTask(zkItem, jedisPool);
			}
		}
	}
	
	private Runnable scanZkItems() {
		
		return new Runnable() {
			
			@Override
			public void run() {
				runTask();
			}
		};
	}
	
	public Runnable insertRedisInst(RedisItem redisItem,RedisBean redisBean,JedisPool jedisPool) {
		return new Runnable() {
			
			@Override
			public void run() {
				Jedis jedis = null;
				String info = null;
				long dbsize = 0;
				try {
					jedis = jedisPool.getResource();
					info = jedis.info();
					dbsize = jedis.dbSize();
				} catch (JedisConnectionException e) {
//					e.printStackTrace();
					if (RedisDbConst.ItemStatus.Offline != redisItem.getStatus()) {
						RedisItem bean = new RedisItem();
						bean.setStatus(RedisDbConst.ItemStatus.Offline);
						redisItemDao.updateWithId(null, bean, redisItem.getId());
					}
				}catch (JedisException e) {
//					e.printStackTrace();
					RedisItem bean = new RedisItem();
					bean.setStatus(RedisDbConst.ItemStatus.Unnormal | redisItem.getStatus());
					redisItemDao.updateWithId(null, bean, redisItem.getId());
				}finally {
					if (jedis != null) {
						try {
							jedis.close();
						} catch (JedisException e2) {
						}
					}
				}
				if (info != null) {
					if (RedisDbConst.ItemStatus.Online != redisItem.getStatus()) {
						RedisItem bean = new RedisItem();
						bean.setStatus(RedisDbConst.ItemStatus.Online);
						redisItemDao.updateWithId(null, bean, redisItem.getId());
					}
					RedisInstMonitor instMonitor = InfoParser.parse(redisItem, redisBean, info);
					instMonitor.setId(idService.genId());
					instMonitor.setObjCount(dbsize);
					redisInstMonitorDao.insert(null, instMonitor);
				}
			}
		};
	}
	
	public void setIdService(IdService idService) {
		this.idService = idService;
	}
	
	public void setRedisItemDao(RedisItemDao redisItemDao) {
		this.redisItemDao = redisItemDao;
	}
	
	public void setRedisInstMonitorDao(RedisInstMonitorDao redisInstMonitorDao) {
		this.redisInstMonitorDao = redisInstMonitorDao;
	}
	
	public ExecutorService getRedisInstMonitorService() {
		return redisInstMonitorService;
	}
	
	public IdService getIdService() {
		return idService;
	}
}
