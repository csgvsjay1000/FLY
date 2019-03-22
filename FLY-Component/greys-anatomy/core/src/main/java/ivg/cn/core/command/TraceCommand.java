package ivg.cn.core.command;

import java.lang.instrument.Instrumentation;

import ivg.cn.core.command.Command.GetEnhancer;
import ivg.cn.core.command.Command.Printer;
import ivg.cn.core.command.annotation.Cmd;
import ivg.cn.core.command.annotation.IndexArg;
import ivg.cn.core.server.Session;

@Cmd(name="trace")
public class TraceCommand implements Command{

	@IndexArg(index = 0, name = "class-pattern")
	private String classPattern;
	
	@IndexArg(index = 1, name = "method-pattern")
	private String methodPattern;
	
	@Override
	public Action getAction() {
		
		return new GetEnhancerAction() {
			
			@Override
			public GetEnhancer action(Session session, Instrumentation inst, Printer printer) throws Throwable {
				
				return null;
			}
		};
		
	}
	
}
