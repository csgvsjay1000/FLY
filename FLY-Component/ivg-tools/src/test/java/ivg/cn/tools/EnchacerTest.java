package ivg.cn.tools;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import ivg.cn.tools.asm.enchance.Enchancer;

public class EnchacerTest {
	
	@Test
	public void testEnchance() {
		
		Enchancer enchancer = new Enchancer();
		enchancer.setAdviceListener(new TestAdviceListener());
		enchancer.setTargetClass(Person.class);
		
		try {
			Object object = enchancer.enchance();
			Method method;
			try {
				method = object.getClass().getMethod("sayHello", String.class);
				try {
					System.out.println(method.invoke(object, "jinjin"));
				} catch (IllegalArgumentException | InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
