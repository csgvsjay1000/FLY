package ivg.cn.core.server;

import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultSessionManager implements SessionManager{

	// 5分钟
    private static final long DURATION_5_MINUTE = 5L * 60 * 1000;

    // 会话超时时间
    private static final long DEFAULT_SESSION_DURATION = DURATION_5_MINUTE;
    
	// 会话ID生成器
	private final AtomicInteger sessionIndexSequence = new AtomicInteger(0);
	
	@Override
	public Session newSession(int javaPid, SocketChannel socketChannel, Charset charset) {
		final int sessionId = sessionIndexSequence.getAndIncrement();
		final Session session = new Session(javaPid, sessionId, DEFAULT_SESSION_DURATION, socketChannel, charset);
		
		return session;
	}

}
