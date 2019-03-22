package ivg.cn.agent;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.MalformedURLException;
import java.util.jar.JarFile;

public class AgentLauncher {

	private static volatile AgentClassLoader agentClassLoader;
	
	public static void premain(String args, Instrumentation inst) {
		main(args, inst);
	}
	
	public static void agentmain(String args, Instrumentation inst) {
		main(args, inst);
	}
	
	private static synchronized void main(String args, Instrumentation inst) {
		
		try {
			final int index = args.indexOf(';');
            final String agentJar = args.substring(0, index);
            final String agentArgs = args.substring(index, args.length());
            
//            String filePath = AgentLauncher.class.getProtectionDomain().getCodeSource().getLocation().getFile();
//            inst.appendToBootstrapClassLoaderSearch(new JarFile(filePath));
            
            final ClassLoader agentLoader = loadOrDefineClassLoader(agentJar);
            
            final Class<?> classOfConfigure = agentLoader.loadClass("ivg.cn.core.Configure");
            // 反序列化成Configure类实例
            final Object objectOfConfigure = classOfConfigure.getMethod("toConfigure", String.class)
                    .invoke(null, agentArgs);

            // JavaPid
            final int javaPid = (Integer) classOfConfigure.getMethod("getJavaPid").invoke(objectOfConfigure);
            
            final Class<?> classOfGaServer = agentLoader.loadClass("ivg.cn.core.server.GaServer");
            
         // 获取GaServer单例
            final Object objectOfGaServer = classOfGaServer
                    .getMethod("getInstance", int.class, Instrumentation.class)
                    .invoke(null, javaPid, inst);

            // gaServer.isBind()
            final boolean isBind = (Boolean) classOfGaServer.getMethod("isBind").invoke(objectOfGaServer);

            if (!isBind) {
                try {
                    classOfGaServer.getMethod("bind", classOfConfigure).invoke(objectOfGaServer, objectOfConfigure);
                } catch (Throwable t) {
                    classOfGaServer.getMethod("destroy").invoke(objectOfGaServer);
                    throw t;
                }

            }
            
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	
	private static ClassLoader loadOrDefineClassLoader(String agentJar) throws Throwable {
		if (null != agentClassLoader) {
			return agentClassLoader;
		}
		agentClassLoader = new AgentClassLoader(agentJar);
		Spy.initForAgentLauncher(AgentLauncher.class.getDeclaredMethod("resetAgentLoader"));
		
		return agentClassLoader;
	}
	
	public static synchronized void resetAgentLoader() {
		try {
			agentClassLoader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		agentClassLoader = null;
		System.gc();
		System.out.println("System.gc");
	}
	
}
