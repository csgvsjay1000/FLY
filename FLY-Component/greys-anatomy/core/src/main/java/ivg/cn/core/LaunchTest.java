package ivg.cn.core;

import java.io.IOException;
import java.lang.management.ManagementFactory;

import ivg.cn.core.server.GaServer;
import ivg.cn.core.util.GaClassUtils;


public class LaunchTest {

	
	public static void main(String[] args) throws IOException {
		String name = ManagementFactory.getRuntimeMXBean().getName();
		String pid = name.split("@")[0]; 
		System.out.println(pid);
		while (true) {
			try {
				
//				loader = new AgentClassLoader("F:\\greys-core.jar");
//				Class<?> clazz = null;
//				try {
//					clazz = loader.loadClass("ivg.cn.core.server.GaServer");
//				} catch (ClassNotFoundException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				Thread.sleep(2000);
//				GaClassUtils.scanPackage(clazz.getClassLoader(), "ivg.cn.core");
//				
//				Thread.sleep(20000);
//				loader.close();
				Thread.sleep(10000);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
}
