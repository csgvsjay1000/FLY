package ivg.cn.dbsplit.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.springframework.util.StringUtils;

public class ReflectionUtil {

	/**获取类的有效属性*/
	public static List<Field> getClassEffectiveFields(Class<? extends Object> clazz) {
		List<Field> effectiveFields = new LinkedList<Field>();
		
		while (clazz != null) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				if (!field.isAccessible()) {
					// 不可访问
				}
				effectiveFields.add(field);
			}
			clazz = clazz.getSuperclass();
		}
		
		return effectiveFields;
	}
	
	/**
	 * 根据属性名生成set方法
	 * */
	public static String fieldName2SetterName(String fieldName) {
		return "set" + StringUtils.capitalize(fieldName);
	}
	
	/**
	 * 查找枚举类型的set方法
	 * */
	public static Method searchEnumSetter(Class<?> clazz, String methodName) {
		Method[] methods = clazz.getMethods();

		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				if (method.getParameterCount() > 0) {
					Class<?> paramType = method.getParameterTypes()[0];
					if (Enum.class.isAssignableFrom(paramType))
						return method;
				}
			}
		}

		return null;
	}
	
}
