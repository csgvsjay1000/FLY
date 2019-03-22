package ivg.cn.core.server;

import java.io.IOException;

public interface CommandHandler {

	/**
	 * 解析命令行并执行
	 * */
	void executeCommand(final String line, final Session session) throws IOException;
	
}
