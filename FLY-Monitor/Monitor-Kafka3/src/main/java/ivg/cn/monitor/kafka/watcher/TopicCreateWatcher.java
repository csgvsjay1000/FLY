package ivg.cn.monitor.kafka.watcher;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.data.Stat;

import ivg.cn.monitor.kafka.bo.TopicInfo;
import ivg.cn.monitor.kafka.listener.TopicEventListener;
import ivg.cn.monitor.kafka.listener.ZKListener;

public class TopicCreateWatcher extends AbstractWatcher{
	String __consumer_offsets = "__consumer_offsets";
	
	TopicDeleteWatcher topicDeleteWatcher;
	ConcurrentHashMap<String, Boolean> topicMap = new ConcurrentHashMap<String, Boolean>();

	public TopicCreateWatcher(ZooKeeper zooKeeper, String path) {
		super(zooKeeper, path);
	}

	@Override
	public void watch() {
		Stat stat = new Stat();
		try {
			List<String> topics = zooKeeper.getChildren(path, new Watcher() {
				
				@Override
				public void process(WatchedEvent event) {
					log.info("watchTopic() == "+event.toString());
					if (event.getType() == EventType.NodeChildrenChanged) {
						// 子节点数据变更，新增或删除topic

					}
					watch();
				}
			}, stat );
			if (topics != null && topics.size()>0) {
				for (String topic : topics) {
					if (topic.equals(__consumer_offsets)) {
						continue;
					}
					if (!topicMap.containsKey(topic) && !topicDeleteWatcher.containsKey(topic)) {
						// 有新增topic
						topicMap.putIfAbsent(topic, true);
						TopicInfo topicInfo = addChangeWatcher(path+"/"+topic);
						for(ZKListener zkListener : zkListeners){
							if (zkListener instanceof TopicEventListener) {
								((TopicEventListener) zkListener).createEvent(topicInfo);
							}
						}
					}
				}
			}
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
		
	}


	private TopicInfo addChangeWatcher(String path) {
		TopicChangeWatcher watcher = new TopicChangeWatcher(zooKeeper, path);
		watcher.watch();
		// 返回当前info信息
		return watcher.getPreTopicInfo();
	}
	
	public void setTopicDeleteWatcher(TopicDeleteWatcher topicDeleteWatcher) {
		this.topicDeleteWatcher = topicDeleteWatcher;
	}
}
