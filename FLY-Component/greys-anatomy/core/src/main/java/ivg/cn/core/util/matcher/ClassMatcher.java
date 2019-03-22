package ivg.cn.core.util.matcher;

import java.lang.annotation.Annotation;
import java.util.Collection;

import ivg.cn.core.util.GaReflectUtils;

/**
 * 类匹配器
 * */
public class ClassMatcher extends ReflectMatcher<Class<?>>{

	private int type;  // 类型，eg: class, interface, enum
	
	public ClassMatcher(int modifier, int type, Matcher<String> name,
			Collection<Matcher<Class<? extends Annotation>>> annotations) {
		super(modifier, name, annotations);
		this.type = type;
	}
	
	/** 只匹配名称 */
	public ClassMatcher(Matcher<String> name) {
		this(GaReflectUtils.DEFAULT_MOD, GaReflectUtils.DEFAULT_TYPE, name, null);
	}
	
	@Override
	int getTargetModifiers(Class<?> target) {
		return target.getModifiers();
	}

	@Override
	String getTargetName(Class<?> target) {
		return target.getName();
	}

	@Override
	Annotation[] getTargetAnnotations(Class<?> target) {
		return target.getAnnotations();
	}

	@Override
	boolean reflectMatching(Class<?> target) {
		return matchingType(target);  // 类型匹配
	}
	
	private boolean matchingType(Class<?> target) {
		if (GaReflectUtils.DEFAULT_TYPE == type) {
			// 如果是全匹配，则返回true
			return true;
		}
		return false;
	}


}
