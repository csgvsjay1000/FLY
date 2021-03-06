package ivg.cn.monitor.redis.parser;

import java.math.BigDecimal;
import java.util.Date;

import ivg.cn.monitor.redis.GlobalCache;
import ivg.cn.monitor.redis.RedisBean;
import ivg.cn.monitor.redis.entity.RedisInstMonitor;
import ivg.cn.monitor.redis.entity.RedisItem;

/**
# Clients
connected_clients:2
client_recent_max_input_buffer:2
client_recent_max_output_buffer:0
blocked_clients:0

# Memory
used_memory:2032920
used_memory_human:1.94M
used_memory_rss:3637248
used_memory_rss_human:3.47M
maxmemory:1073741824
maxmemory_human:1.00G
mem_fragmentation_ratio:1.84

# Stats
total_commands_processed:11752
keyspace_hits:0
keyspace_misses:0

# Replication
role:master
master_repl_offset:64319715
slave_repl_offset:64319813

# Keyspace
db0:keys=11000,expires=0,avg_ttl=0


 * */
public class InfoParser {
	private static final String connected_clients = "connected_clients:";
	private static final String used_memory = "used_memory:";
	private static final String maxmemory = "maxmemory:";
	private static final String mem_fragmentation_ratio = "mem_fragmentation_ratio:";
	private static final String total_commands_processed = "total_commands_processed:";
	private static final String keyspace_hits = "keyspace_hits:";
	private static final String keyspace_misses = "keyspace_misses:";
	private static final String role = "role:";
	private static final String usedcpusys = "used_cpu_sys:";
	
	public static RedisInstMonitor parse(RedisItem redisItem,RedisBean redisBean,String info) {
		String[] lines = info.split("\n");
		
		RedisInstMonitor instMonitor = new RedisInstMonitor();
		
		for (String line : lines) {
			line = line.trim();
			if (line.startsWith(connected_clients)) {
				// 发包数量
				String value = line.substring(connected_clients.length());
				instMonitor.setConnections(Integer.parseInt(value));
			}else if (line.startsWith(used_memory)) {
				// 发包数量
				String value = line.substring(used_memory.length());
				instMonitor.setUsedMemory(Long.parseLong(value));
			}else if (line.startsWith(maxmemory)) {
				// 发包数量
				String value = line.substring(maxmemory.length());
				instMonitor.setMaxMemory(Long.parseLong(value));
			}else if (line.startsWith(mem_fragmentation_ratio)) {
				// 发包数量
				String value = line.substring(mem_fragmentation_ratio.length());
				instMonitor.setMemFragmentationRatio(new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_DOWN));
			}else if (line.startsWith(keyspace_hits)) {
				// 发包数量
				String value = line.substring(keyspace_hits.length());
				instMonitor.setKeyspaceHits(Integer.parseInt(value));
			}else if (line.startsWith(keyspace_misses)) {
				// 发包数量
				String value = line.substring(keyspace_misses.length());
				instMonitor.setKeyspaceMisses(Integer.parseInt(value));
			}else if (line.startsWith(role)) {
				// 发包数量
				String value = line.substring(role.length());
				instMonitor.setRole(value);
			}else if (line.startsWith(total_commands_processed)) {
				// 发包数量
				String value = line.substring(total_commands_processed.length());
				Integer now = Integer.valueOf(value);
				String qps = GlobalCache.getInstance().calculateQPS(redisBean, now);
				instMonitor.setQps(new BigDecimal(qps));
			}else if (line.startsWith(usedcpusys)) {
				// 发包数量
				String value = line.substring(usedcpusys.length());
				float now = Float.valueOf(value);
				String cpu = GlobalCache.getInstance().calculateCPU(redisBean, now);
				instMonitor.setCpuRatio(new BigDecimal(cpu).setScale(2, BigDecimal.ROUND_HALF_DOWN));
			}
		}
		
		Date date = new Date();
		instMonitor.setCreateDate(date);
		instMonitor.setItemId(redisItem.getId());
		instMonitor.setRedisIp(redisBean.getIp());
		instMonitor.setRedisPort(redisBean.getPort());
		
		return instMonitor;
	}
	
}
