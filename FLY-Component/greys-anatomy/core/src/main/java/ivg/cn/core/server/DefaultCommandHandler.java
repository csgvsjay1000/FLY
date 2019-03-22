package ivg.cn.core.server;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.apache.commons.lang3.StringUtils;

import ivg.cn.core.advisor.AdviceListener;
import ivg.cn.core.command.Command;
import ivg.cn.core.command.Command.Printer;
import ivg.cn.core.command.Commands;
import ivg.cn.core.command.ShutdownCommand;
import ivg.cn.core.exception.CommandException;

public class DefaultCommandHandler implements CommandHandler{

	private GaServer gaServer;
	
	private Instrumentation inst;
	
	public DefaultCommandHandler(GaServer gaServer, Instrumentation inst) {
		this.gaServer = gaServer;
		this.inst = inst;
	}
	

	@Override
	public void executeCommand(String line, Session session) throws IOException {
		
		if (StringUtils.isBlank(line)) {
			// 如果是空字符串，绘制提示符  >ga?
		}
		
		// 1、获取Command
		// 2、执行Command
		
		try {
			Command command = Commands.getInstance().newCommand(line);
			execute(session, command);
			
			if (command instanceof ShutdownCommand) {
				// 如果是shutdown, greys退出
				gaServer.destroy();
			}
			
		} catch (CommandException e) {
			// 命令输出
			e.printStackTrace();
		}
		
	}
	
	private void execute(Session session, Command command) {
		
		Printer printer = new Printer() {
			
		};
		
		try {
			// 执行命令
			// 1、先获取command对应的action
			// 2、判断action类别
			
			Command.Action action = command.getAction();
			if (action instanceof Command.GetEnhancerAction) {
				// 如果是增强类Action
				// 1、执行动作并获取增强器
				Command.GetEnhancer getEnhancer = ((Command.GetEnhancerAction) action).action(session, inst, printer);
				
				AdviceListener listener = getEnhancer.getAdviceListener();
				
			}
			if (action instanceof Command.RowAction) {
				// 行动作
				((Command.RowAction) action).action(session, inst, printer);
			}
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
	}
	
	private void write(SocketChannel socketChannel, ByteBuffer buffer) throws IOException {
		while (buffer.hasRemaining() && socketChannel.isConnected()) {
			socketChannel.write(buffer);
		}
	}

}
