package ivg.cn.monitor.zk.parser;

import java.util.Date;

import ivg.cn.monitor.zk.entity.ZkInstMonitor;
import ivg.cn.monitor.zk.entity.ZkItem;

/**
 * <pre>
Zookeeper version: 3.4.13-2d71af4dbe22557fda74f9a9b4309b15a7487f03, built on 06/29/2018 04:05 GMT
Latency min/avg/max: 0/0/0
Received: 3
Sent: 2
Connections: 1
Outstanding: 0  // 未完成的请求
Zxid: 0x0
Mode: standalone
Node count: 4  // 节点数
 * */
public class SrvrParser {
	private static final String Latency = "Latency min/avg/max: ";
	private static final String Received = "Received: ";
	private static final String Sent = "Sent: ";
	private static final String Connections = "Connections: ";
	private static final String Mode = "Mode: ";
	private static final String NodeCount = "Node count: ";
	
	public static ZkInstMonitor parse(ZkItem zkItem,String result) {
		
		String[] lines = result.split("\n");
		
		ZkInstMonitor instMonitor = new ZkInstMonitor();
		
		for (String line : lines) {
			if (line.startsWith(Latency)) {
				String value = line.substring(Latency.length());
				String[] lan = value.split("/");
				if (lan != null && lan.length==3) {
					instMonitor.setMinLatency(Integer.parseInt(lan[0]));
					instMonitor.setAvgLatency(Integer.parseInt(lan[1]));
					instMonitor.setMaxLatency(Integer.parseInt(lan[2]));
				}
			}else if (line.startsWith(Received)) {
				// 收包数量
				String value = line.substring(Received.length());
				instMonitor.setRecvPacketQty(Long.parseLong(value));
				
			}else if (line.startsWith(Sent)) {
				// 发包数量
				String value = line.substring(Sent.length());
				instMonitor.setSendPacketQty(Long.parseLong(value));
				
			}else if (line.startsWith(Connections)) {
				// 连接数
				String value = line.substring(Connections.length());
				instMonitor.setConnections(Integer.parseInt(value));
			}else if (line.startsWith(Mode)) {
				// mode
				String value = line.substring(Mode.length());
				instMonitor.setMode(value);
			}else if (line.startsWith(NodeCount)) {
				// 节点数
				String value = line.substring(NodeCount.length());
				instMonitor.setNodeCount(Integer.parseInt(value));
			}
		}
		instMonitor.setItemId(zkItem.getId());
		instMonitor.setZkIp(zkItem.getZkIp());
		instMonitor.setZkPort(zkItem.getZkPort());
		
		Date date = new Date();
		instMonitor.setCreateDate(date);
		instMonitor.setUpdateDate(date);
		
		instMonitor.setCreater(1L);
		instMonitor.setUpdater(1L);
		
		return instMonitor;
	}
	
}
