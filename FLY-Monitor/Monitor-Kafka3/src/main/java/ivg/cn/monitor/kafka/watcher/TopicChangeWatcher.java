package ivg.cn.monitor.kafka.watcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.data.Stat;

import com.alibaba.fastjson.JSONObject;

import ivg.cn.monitor.kafka.bo.Partitions;
import ivg.cn.monitor.kafka.bo.TopicInfo;
import ivg.cn.monitor.kafka.listener.TopicEventListener;
import ivg.cn.monitor.kafka.listener.ZKListener;

public class TopicChangeWatcher extends AbstractWatcher{

	volatile TopicInfo preTopicInfo;
	
	public TopicChangeWatcher(ZooKeeper zooKeeper, String path) {
		super(zooKeeper, path);
		
	}

	@Override
	public void watch() {
		Stat stat = new Stat();
		
		try {
			byte[] data = zooKeeper.getData(path, new Watcher() {
				
				@Override
				public void process(WatchedEvent event) {
					log.info("getTopicDataAndWatch() == "+event.toString());
					if (event.getType() == EventType.NodeDataChanged) {
						// 子节点数据变更，一般有新的子节点数据添加进来
						watch();
					}
				}
			}, stat);
			JSONObject jsonObject = JSONObject.parseObject(new String(data));
			jsonObject = jsonObject.getJSONObject("partitions");
			
			TopicInfo topicInfo = new TopicInfo();
			List<Partitions> pList = new ArrayList<>();
			for(Entry<String, Object> entry : jsonObject.entrySet()){
				Partitions partitions = new Partitions();
				partitions.setPart(Integer.valueOf(entry.getKey()));
				@SuppressWarnings("unchecked")
				List<Integer> vaList = (List<Integer>) entry.getValue();
				partitions.setIsr(vaList);
				partitions.setLeader(vaList.get(0));
				pList.add(partitions);
			}
			topicInfo.setPartitions(pList);
			topicInfo.setTopic(getTopic());
			
			
			for(ZKListener zkListener : zkListeners){
				if (zkListener instanceof TopicEventListener) {
					((TopicEventListener) zkListener).changeEvent(preTopicInfo, topicInfo);
				}
			}
			preTopicInfo = topicInfo;
			
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public TopicInfo getPreTopicInfo() {
		return preTopicInfo;
	}
	
	private String getTopic() {
		return path.substring(path.lastIndexOf("/")+1);
	}

}
