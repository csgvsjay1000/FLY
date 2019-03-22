package ivg.cn.tools.asm.enchance;

import org.objectweb.asm.commons.Method;

public interface AsmMethods {

	class MethodFinder{
		
		private MethodFinder() {
		}
		
		static Method getAsmMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
			return Method.getMethod(getJavaMethodUnSafe(clazz, methodName, parameterTypes));
		}
		
		static java.lang.reflect.Method getJavaMethodUnSafe(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
			try {
				return clazz.getDeclaredMethod(methodName, parameterTypes);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
			}
		}
		
	}
	
	Method AdviceWeaver_methodOnBegin = MethodFinder.getAsmMethod(
			AdviceWeaver.class,
			"methodOnBegin",
			int.class,
			Object.class,
			Object[].class
			);
	
	Method AdviceWeaver_methoOnReturn = MethodFinder.getAsmMethod(
			AdviceWeaver.class,
			"methoOnReturn",
			Object.class
			);
	
	
	
}
