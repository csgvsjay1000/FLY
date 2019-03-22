package ivg.cn.core.server;

import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

public class Session {

	private static final AtomicInteger lockTxSeq = new AtomicInteger(0);
	// 空锁
	private static final int LOCK_TX_EMPTY = -1;
	
	final int javaPid;
	final int sessionId;
	final long sessionDuration; 
	final SocketChannel socketChannel; 
	final Charset charset;
	
	private volatile long gmtLastTouch;  // 会话最后一次触摸时间
	
	//  会话锁
	private AtomicInteger lockTx = new AtomicInteger(LOCK_TX_EMPTY);
	
	Session(int javaPid, int sessionId, long sessionDuration, SocketChannel socketChannel, Charset charset) {
		this.javaPid = javaPid;
		this.sessionId = sessionId;
		this.sessionDuration = sessionDuration;
		this.socketChannel = socketChannel;
		this.charset = charset;
		this.gmtLastTouch = System.currentTimeMillis();
	}
	
	public boolean tryLock() {
		// 如果当前lockTx为空锁，代表没有锁，则可以获取锁
		return lockTx.compareAndSet(LOCK_TX_EMPTY, lockTxSeq.getAndIncrement());
	}
	
	public void unlock() {
		int currentLock = lockTx.get();
		if (LOCK_TX_EMPTY == currentLock) {
			// 当前没有被锁，不需要解锁
			return;
		}
		if (!lockTx.compareAndSet(currentLock, LOCK_TX_EMPTY)) {  // 把锁置为空锁，即为解锁
			// 如果没有成功，说明开发tryLock/unlock没有成对出现，开发写错了
			throw new IllegalStateException();
		}
	}
	
	public SocketChannel getSocketChannel() {
		return socketChannel;
	}
}
