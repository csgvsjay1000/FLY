package ivg.cn.tools.dislock;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ZkDisLock implements DisLock{
	static Logger logger = LoggerFactory.getLogger(ZkDisLock.class);

	/** 分隔符 */
	private final static String Sper = "/";
	
	private final static String ROOT_NODE = "/fly";
	
	/**  竞争者节点，每个想尝试获取锁的竞争者，都要先获取该节点 */
	private final static String COMPERTITOR_NODE = "ID";
	
	private String lockPath = null;
	
	ZooKeeper zooKeeper;
	
	String thisCompertitorPath = null;
	
	String zkHost;
	volatile boolean closed = false;
	
	String lockName;
	
	public void init() {
		openConn();
		try {
			createRootNode();
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void init(String zkHost, String lookName) {
		this.zkHost = zkHost;
		this.lockName = lookName;
		openConn();
		try {
			createRootNode();
		} catch (KeeperException e) {
		} catch (InterruptedException e) {
		}
	}
	
	/**
	 * 尝试获取分布式锁，若获取成功返回该锁的节点，释放锁需要该返回值
	 * */
	public DisLockResult tryLock(String resources, long timeoutMillis) {
		checkConn();
		List<String> compertitors = null;
		String thisPath ="";
		String resNode = lockPath + Sper + COMPERTITOR_NODE+resources;
		try {

			thisPath = createCompertitorNode(resNode);
			compertitors = zooKeeper.getChildren(resNode, false);
			
		} catch (KeeperException e) {
		} catch (InterruptedException e) {
		}
		DisLockResult result = new DisLockResult(thisPath);
		if (compertitors == null) {
			return result;
		}
		Collections.sort(compertitors);
		int index = compertitors.indexOf(thisPath.substring((resNode+Sper).length()));
		if (index == 0) {
			// 自己是最小节点, 可以获取锁
			result.setIdPath(resNode);
			result.setSuccess(true);
		}else if (index > 0) {
			// 说明自己不是最小节点, 不能获取锁
		}
		return result;
	}

	/**
	 * 释放锁
	 * */
	@Override
	public void release(String resource) {
		// 释放锁只需删除节点即可
		try {
			zooKeeper.delete(resource, -1);
		} catch (InterruptedException e) {
		} catch (KeeperException e) {
		}
	}
	
	@Override
	public void release(DisLockResult resource, boolean deleteIdPath) {
		// TODO Auto-generated method stub
		// 释放锁只需删除节点即可
		try {
			zooKeeper.delete(resource.getPath(), -1);
			if (deleteIdPath) {
				this.recursionDelete(resource.getIdPath());
			}
		} catch (InterruptedException e) {
		} catch (KeeperException e) {
		}
	}
	
	/**
	 * 创建竞争者节点
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 * */
	private String createCompertitorNode(String resources) throws KeeperException, InterruptedException {
	
		Stat lockStat = zooKeeper.exists(resources, false);
		if (lockStat == null) {
			// 如果锁的路径不存在, 则创建锁节点, 定时删除
			try {
				zooKeeper.create(resources, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			} catch (KeeperException e) {
				if (e.code() == Code.NODEEXISTS) {
					// 节点存在不用创建
				}
			}
		}
		String comPath = resources+Sper;
		return zooKeeper.create(comPath, null, Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
	}
	
	public void openConn() {
		
		try {
			CountDownLatch latch = new CountDownLatch(1);
			zooKeeper = new ZooKeeper(zkHost, 5000, new Watcher() {
				
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
			logger.info("ZkDisLock.openConn success. "+zooKeeper);
		} catch (IOException  |InterruptedException e ) {
			logger.error("openConn err,",e);
			if (zooKeeper != null) {
				this.close();
			}
		}
	}
	
	public void close() {
		try {
			zooKeeper.close();
			closed = true;
			logger.error("ZkDisLock.close the connection[{}] closed",zkHost);
		} catch (InterruptedException e) {
			logger.error("close err,",e);
		}
	}
	
	/**
	 * 检查连接是否断开，若已断开则重新连接
	 * */
	private void checkConn() {
		if (closed) {
			openConn();
		}
	}
	
	private void createRootNode() throws KeeperException, InterruptedException {
		Stat rootStat = zooKeeper.exists(ROOT_NODE, false);
		
		String rootPath = null;
		
		if (null == rootStat) {
			// 创建一个永久根节点
			rootPath = zooKeeper.create(ROOT_NODE, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}else {
			rootPath = ROOT_NODE;
		}
		
		String lockNodePath = rootPath + Sper + lockName;
		Stat lockStat = zooKeeper.exists(lockNodePath, false);
		if (lockStat == null) {
			// 如果锁的路径不存在, 则创建锁节点
			lockPath = zooKeeper.create(lockNodePath, null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		}else {
			lockPath = lockNodePath;
		}
	}

	public String getZkHost() {
		return zkHost;
	}

	public void setZkHost(String zkHost) {
		this.zkHost = zkHost;
	}

	public String getLockName() {
		return lockName;
	}

	public void setLockName(String lockName) {
		this.lockName = lockName;
	}

	public void recursionDelete(String path) {
		// 1、先判断是否存在子节点，若存在则遍历子节点，若不存在，则删除当前节点
		
		List<String> children = null;
		try {
			children = zooKeeper.getChildren(path, false);
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (children == null || children.size() == 0) {
			try {
				zooKeeper.delete(path, -1);
			} catch (InterruptedException e) {
			} catch (KeeperException e) {
			}
		}else {
			for (String string : children) {
				recursionDelete(path+"/"+string);
			}
			try {
				zooKeeper.delete(path, -1);
			} catch (InterruptedException e) {
			} catch (KeeperException e) {
			}
		}
		
	}
}
