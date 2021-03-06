package ivg.cn.monitor.comp.zk;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import ivg.cn.monitor.UtilAll;
import ivg.cn.monitor.comp.zk.vo.ZKNodeData;

public class ZKApi {

	static Logger logger = LoggerFactory.getLogger(ZKApi.class);
	
	ZooKeeper zk;
	String zkHost;
	volatile boolean closed = false;
	
	public ZKApi() {
	}
	
	public void openConn(String zkHost) {
		this.zkHost = zkHost;
		try {
			CountDownLatch latch = new CountDownLatch(1);
			zk = new ZooKeeper(zkHost, 5000, new Watcher() {
				
				@Override
				public void process(WatchedEvent event) {
					System.out.println(event);
					if ( KeeperState.SyncConnected == event.getState()) {
						latch.countDown();
						closed = false;
					}
				}
			});
			latch.await();
			logger.info("ZKApi.openConn success. "+zk);
		} catch (IOException  |InterruptedException e ) {
			logger.error("openConn err,",e);
			if (zk != null) {
				this.close();
			}
		}
	}
	
	public List<String> listChildren(String path) {
		checkConn();

		try {
			return zk.getChildren(path, false);
		} catch (KeeperException e) {
			if ( Code.CONNECTIONLOSS == e.code()) {
				logger.error("listChildren err,",e);
				this.close();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ZKNodeData queryNodeData(String path) {
		checkConn();
		
		Stat stat = new Stat();
		byte[] data = null;
		try {
			data = zk.getData(path, false, stat);
		} catch (KeeperException e) {
			if ( Code.CONNECTIONLOSS == e.code()) {
				logger.error("queryNodeData err,",e);
				this.close();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ZKNodeData nodeData = new ZKNodeData();
		BeanUtils.copyProperties(stat, nodeData);
		nodeData.setCtime(UtilAll.dateFormat(stat.getCtime()));
		nodeData.setMtime(UtilAll.dateFormat(stat.getMtime()));
		if (data != null) {
			nodeData.setData(data);
		}
		
		return nodeData;
	}
	
	public void delete(String path, int version) {
		try {
			zk.delete(path, version);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			if ( Code.CONNECTIONLOSS == e.code()) {
				logger.error("delete err,",e);
				this.close();
			}
		}
	}
	
	/**
	 * 递归删除节点，和子节点
	 * */
	public void recursionDelete(String path) {
		// 1、先判断是否存在子节点，若存在则遍历子节点，若不存在，则删除当前节点
		
		List<String> children = null;
		try {
			children = zk.getChildren(path, false);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (children == null || children.size() == 0) {
			try {
				zk.delete(path, -1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (KeeperException e) {
				e.printStackTrace();
			}
		}else {
			for (String string : children) {
				recursionDelete(path+"/"+string);
			}
		}
		
	}
	
	public void close() {
		try {
			zk.close();
			closed = true;
			logger.error("ZKApi.close the connection[{}] closed",zkHost);
		} catch (InterruptedException e) {
			logger.error("close err,",e);
		}
	}
	
	/**
	 * 检查连接是否断开，若已断开则重新连接
	 * */
	private void checkConn() {
		if (closed) {
			openConn(zkHost);
		}
	}
}
