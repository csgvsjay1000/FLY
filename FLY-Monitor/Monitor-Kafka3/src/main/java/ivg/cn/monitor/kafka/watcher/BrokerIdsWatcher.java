package ivg.cn.monitor.kafka.watcher;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.data.Stat;

import com.alibaba.fastjson.JSONObject;

import ivg.cn.monitor.kafka.bo.NodeBO;
import ivg.cn.monitor.kafka.listener.BrokerListener;
import ivg.cn.monitor.kafka.listener.ZKListener;

public class BrokerIdsWatcher extends AbstractWatcher{

	ConcurrentHashMap<Integer, NodeBO> nodeMap = new ConcurrentHashMap<Integer, NodeBO>();
	
	public BrokerIdsWatcher(ZooKeeper zooKeeper, String path) {
		super(zooKeeper, path);
	}

	@Override
	public void watch() {
		Stat stat = new Stat();
		try {
			List<String> ids = zooKeeper.getChildren(path, new Watcher() {
				
				@Override
				public void process(WatchedEvent event) {
					log.info("getBrokersIds() == "+event.toString());
					if (event.getType() == EventType.NodeChildrenChanged) {
						// 子节点数据变更，新增或删除
						watch();
					}
				}
			}, stat);
			if (ids != null && ids.size()>0) {
				for (String id : ids) {
					int intID = Integer.valueOf(id);
					if (!nodeMap.containsKey(intID)) {
						NodeBO nodeBO = getDataAndWatch(intID);
						if (nodeBO != null) {
							nodeMap.putIfAbsent(intID, nodeBO);
							// 新的节点代表上线了
							for(ZKListener listener : zkListeners){
								if (listener instanceof BrokerListener) {
									((BrokerListener) listener).online(nodeBO);
								}
							}
						}
					}
				}
			}
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	private NodeBO getDataAndWatch(int id) {
		
		Stat stat = new Stat();
		
		try {
			 byte[] data = zooKeeper.getData(path+"/"+id, new Watcher() {
				
				@Override
				public void process(WatchedEvent event) {
					log.info("getBrokersIds() == "+event.toString());
					if (event.getType() == EventType.NodeDataChanged) {
						// 子节点数据变更，一般有新的子节点数据添加进来
						getDataAndWatch(id);
					}else if (event.getType() == EventType.NodeDeleted) {
						// 节点下线
						for(ZKListener listener : zkListeners){
							if (listener instanceof BrokerListener) {
								((BrokerListener) listener).offline(nodeMap.get(id));
							}
						}
						nodeMap.remove(id);
					}
				}
			}, stat);
			JSONObject json = JSONObject.parseObject(new String(data));
			NodeBO nodeBO = new NodeBO(id);
			Integer jmxPort = json.getInteger("jmx_port");
			Integer port = json.getInteger("port");
			String host = json.getString("host");
			nodeBO.setHost(host);
			nodeBO.setPort(port);
			nodeBO.setJmxPort(jmxPort);
			return nodeBO;
			
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean isAlive(int brokerId) {
		return nodeMap.containsKey(brokerId);
	}

	public NodeBO nodeBO(int brokerId) {
		return nodeMap.get(brokerId);
	}
	
}
