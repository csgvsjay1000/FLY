package ivg.cn.core.command;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;

import ivg.cn.core.command.annotation.Cmd;
import ivg.cn.core.server.Session;
import ivg.cn.core.util.LogUtil;

@Cmd(name="shutdown")
public class ShutdownCommand implements Command{
	private final Logger logger = LogUtil.getLogger();

	@Override
	public Action getAction() {
		
		return new RowAction() {
			
			@Override
			public void action(Session session, Instrumentation inst, Printer printer) throws Throwable {
				// 重置agentClassLoader
				
				reset();
			}
		};
	}
	
	private void reset() throws Throwable {
		// 执行AgentLaunch.reset()方法
		Class<?> spyClass = loadClassFromGreysClassLoader(ShutdownCommand.class.getClassLoader(), "ivg.cn.agent.Spy");
		if (spyClass != null) {
			Method method = (Method) FieldUtils.getField(spyClass, "AGENT_RESET_METHOD").get(null);
			method.invoke(null);
			Method cleanMethod = spyClass.getMethod("clean");
			cleanMethod.invoke(null);
		}

	}
	
	private Class<?> loadClassFromGreysClassLoader(final ClassLoader classLoader, String spyClassName) {
		try {
			return classLoader.loadClass(spyClassName);
		} catch (ClassNotFoundException e) {
			logger.warn("Spy load failed from GreysClassLoader, that is impossible!", e);
            return null;
		}
	}

}
