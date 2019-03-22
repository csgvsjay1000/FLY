package ivg.cn.tools.asm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;

public class MainTest {

	public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, MalformedURLException {
		AgentClassLoader classLoader = new AgentClassLoader("F:\\asmtest");
		
		Class<?> class1 = classLoader.loadClass("ivg.cn.tools.asm.Account");
		Object inObject = null;
		try {
			inObject = class1.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Method method = class1.getMethod("operation");
			method.invoke(inObject);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
