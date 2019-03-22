package ivg.cn.core.server;

import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * 会话管理器
 * */
public interface SessionManager {

	/**  创建Session */
	Session newSession(int javaPid, SocketChannel socketChannel, Charset charset);
	
}
