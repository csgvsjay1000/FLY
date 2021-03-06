package ivg.cn.monitor.redis.parser;

import java.util.Date;
import java.util.Map;

import ivg.cn.monitor.redis.RedisDbConst;
import ivg.cn.monitor.redis.entity.MasterItemMonitor;

/**
 * {role-reported=master, info-refresh=1238, config-epoch=1, 
 * last-ping-sent=0, role-reported-time=411668742, 
 * ip=192.168.5.131, quorum=2, flags=master, parallel-syncs=1, 
 * num-slaves=2, link-pending-commands=0, failover-timeout=180000, 
 * port=10311, num-other-sentinels=2, name=ivgmaster, last-ok-ping-reply=515, 
 * last-ping-reply=515, runid=25fad895ebcd4e5e1c305ff53efa64a2e3c5b6f2, 
 * link-refcount=1, down-after-milliseconds=30000}
 * */
public class SentinelMasterParser {

	
	public static MasterItemMonitor parse(Map<String, String> map) {
		MasterItemMonitor itemMonitor = new MasterItemMonitor();
		
		itemMonitor.setMasterName(map.get("name"));
		itemMonitor.setMasterRedisIp(map.get("ip"));
		itemMonitor.setMasterRedisPort(Integer.valueOf(map.get("port")));
		itemMonitor.setCurrentSlaves(Integer.valueOf(map.get("num-slaves")));
		itemMonitor.setCurrentSentinels(Integer.valueOf(map.get("num-other-sentinels"))+1);
		itemMonitor.setQuorum(Integer.valueOf(map.get("quorum")));
		itemMonitor.setDownAfterMilliseconds(Integer.valueOf(map.get("down-after-milliseconds")));
		itemMonitor.setFailoverTimeout(Integer.valueOf(map.get("failover-timeout")));
		itemMonitor.setParallelSyncs(Integer.valueOf(map.get("parallel-syncs")));
		itemMonitor.setEnable(RedisDbConst.MasterStatus.OK);
		itemMonitor.setStatus(RedisDbConst.MasterStatus.Normal);
		itemMonitor.setCreateDate(new Date());
		
		return itemMonitor;
	}
	
}
