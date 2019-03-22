package ivg.cn.monitor.kafka.watcher;

import java.util.HashSet;
import java.util.Set;

import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ivg.cn.monitor.kafka.listener.ZKListener;

public abstract class AbstractWatcher {

	Logger log = LoggerFactory.getLogger(getClass());
	
	protected String path;
	
	protected ZooKeeper zooKeeper;
	
	protected Set<ZKListener> zkListeners;
	
	public AbstractWatcher(ZooKeeper zooKeeper,String path) {
		this.zooKeeper = zooKeeper;
		this.path = path;
		zkListeners = new HashSet<>(2);
	}
	
	public void addListener(ZKListener listener) {
		zkListeners.add(listener);
	}
	
	public abstract void watch();  
	
}
