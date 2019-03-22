package ivg.cn.monitor.kafka.watcher;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.data.Stat;

import ivg.cn.monitor.kafka.listener.TopicEventListener;
import ivg.cn.monitor.kafka.listener.ZKListener;

public class TopicDeleteWatcher extends AbstractWatcher{
	
	ConcurrentHashMap<String, Boolean> deleteTopicMap = new ConcurrentHashMap<String, Boolean>();

	public TopicDeleteWatcher(ZooKeeper zooKeeper, String path) {
		super(zooKeeper, path);
	}

	@Override
	public void watch() {
		Stat stat = new Stat();
		try {
			List<String> topics = zooKeeper.getChildren(path, new Watcher() {
				
				@Override
				public void process(WatchedEvent event) {
					log.info("{} ==> {}",path,event.toString());
					if (event.getType() == EventType.NodeChildrenChanged) {
						// 子节点数据变更，新增或删除topic

					}
					watch();
				}
			}, stat );
			if (topics != null && topics.size()>0) {
				for (String topic : topics) {
					if (!deleteTopicMap.containsKey(topic)) {
						// 有新增topic
						deleteTopicMap.putIfAbsent(topic, true);
						for(ZKListener zkListener : zkListeners){
							if (zkListener instanceof TopicEventListener) {
								((TopicEventListener) zkListener).deleteEvent(topic);
							}
						}
					}
				}
			}
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public boolean containsKey(String topic) {
		return deleteTopicMap.containsKey(topic);
	}

}
