package ivg.cn.monitor.redis.service;

import java.util.Date;
import java.util.List;

import ivg.cn.monitor.redis.MonitorRedisController;
import ivg.cn.monitor.redis.db.RedisSlowLogDao;
import ivg.cn.monitor.redis.db.impl.RedisSlowLogDaoImpl;
import ivg.cn.monitor.redis.entity.RedisItem;
import ivg.cn.monitor.redis.entity.RedisSlowLog;
import ivg.cn.monitor.redis.util.SpringBeanUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.util.Slowlog;

/**
 * 慢日志监控服务
 * */
public class SlowlogService {

	
	final MonitorRedisController monitorRedisController;
	
	private RedisSlowLogDao redisSlowLogDao;
	
    long EPOCH = 1514736000000L;  // 1970年开始的时间戳
	
	public SlowlogService(MonitorRedisController monitorRedisController) {
		this.monitorRedisController = monitorRedisController;
		redisSlowLogDao = SpringBeanUtil.getBean(RedisSlowLogDaoImpl.class);
	}
	
	
	public void submitTask(RedisItem redisItem,JedisPool jedisPool) {
		
		monitorRedisController.getRedisInstMonitorService().submit(processTask(redisItem, jedisPool));
		
	}
	
	public Runnable processTask(RedisItem redisItem,JedisPool jedisPool) {
		return new Runnable() {
			
			@Override
			public void run() {
				Jedis jedis = null;
				List<Slowlog> slowlogs = null;
				try {
					jedis = jedisPool.getResource();
					slowlogs = jedis.slowlogGet();
				}catch (JedisConnectionException e) {
					
				} finally {
					if (jedis != null) {
						jedis.close();
					}
				}
				if (slowlogs != null) {
					for (Slowlog slowlog : slowlogs) {
						
						// 1、先判断该log是否已经记录, 通过id来判断
						slowlog.getId();
						RedisSlowLog redisSlowLog = new RedisSlowLog();
						redisSlowLog.setCmdId(slowlog.getId());
						redisSlowLog.setItemId(redisItem.getId());
						List<RedisSlowLog> result = redisSlowLogDao.select(null, redisSlowLog);
						if (result != null && result.size()>0) {
							// 说明已经存储该日志了，不需要重复存储
						}else {
							redisSlowLog.setId(monitorRedisController.getIdService().genId());
							redisSlowLog.setCmd(slowlog.getArgs().toString());
							redisSlowLog.setCostTime(slowlog.getExecutionTime());
							redisSlowLog.setOccurTime(new Date(slowlog.getTimeStamp()*1000));
							Date date = new Date();
							
							redisSlowLog.setCreateDate(date);
							redisSlowLog.setItemId(redisItem.getId());
							redisSlowLog.setRedisIp(redisItem.getRedisIp());
							redisSlowLog.setRedisPort(redisItem.getRedisPort());
							redisSlowLogDao.insert(null, redisSlowLog);
						}
						
					}
				}
			}
		};
	}
	
}
