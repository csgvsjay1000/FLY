package ivg.cn.agent;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class AgentClassLoader extends URLClassLoader{

	public AgentClassLoader(String agentJar) throws MalformedURLException {
		super(new URL[]{new URL("file:"+agentJar)});
	}
	
	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		
		// 先查看是否已经加载过
		Class<?> loadedClass = findLoadedClass(name);
		if (loadedClass != null) {
			return loadedClass;
		}
		try {
			Class<?> aClass = findClass(name);
			if (resolve) {
				resolveClass(aClass);
			}
			return aClass;
		} catch (Exception e) {
			return super.loadClass(name, resolve);
		}
		
	}
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}

}
