package ivg.cn.dbsplit.reflect;

import java.lang.reflect.Field;
import java.util.List;

public class FieldVisitor<T> {

	private T bean;
	
	public FieldVisitor(T bean) {
		this.bean = bean;
	}
	
	/**
	 * 访问bean的属性
	 * */
	public void visit(FieldHandler fieldHandler) {
		// 1、获取要访问bean的所有属性
		
		List<Field> fields = ReflectionUtil.getClassEffectiveFields(bean.getClass());
		
		int count = 0;
		for (Field field : fields) {
			Object value = null;
			
			try {
				boolean access = field.isAccessible();
				field.setAccessible(true);
				value = field.get(bean);
				if (value != null) {
					if ((value instanceof Number) && ((Number)value).doubleValue() == -1d ) {
						continue;
					}
					if (value instanceof List) {
						continue;
					}
					fieldHandler.handle(count++, field, value);
				}
				field.setAccessible(access);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
}
