package ivg.cn.core.util.matcher;

import java.lang.annotation.Annotation;
import java.util.Collection;

import ivg.cn.core.util.GaReflectUtils;

/**  反射相关匹配器 
 * 
 * 1、类匹配
 * 2、方法匹配
 * 
 * 匹配条件：
 * 1、类或方法的修饰符匹配
 * 2、名称匹配
 * 3、类或方法的annotation匹配
 * 
 * */
public abstract class ReflectMatcher<T> implements Matcher<T>{

	private int modifier;
	
	private Matcher<String> name;  // 名称匹配
	
	// Annotation匹配器
	private Collection<Matcher<Class<? extends Annotation>>> annotations;
	
	public ReflectMatcher(int modifier, Matcher<String> name,
			Collection<Matcher<Class<? extends Annotation>>> annotations) {
		this.modifier = modifier;
		this.name = name;
		this.annotations = annotations;
	}

	@Override
	public boolean matching(T target) {
		// 1、判断模板不能为空
		// 2、匹配修饰符
		return (null != target  // 推空保护
				&& matchingModifier(getTargetModifiers(target))  // 修饰符匹配
				&& matchingName(getTargetName(target))  // 匹配名称  
				&& matchingAnnotation(getTargetAnnotations(target))  // 匹配Annotation 
				&& reflectMatching(target)); 
	}
	
	abstract int getTargetModifiers(T target);
	
	abstract String getTargetName(T target);
	
	abstract Annotation[] getTargetAnnotations(T target);
	
	/** 具体匹配器去实现其他匹配  */
	abstract boolean reflectMatching(T target);
	
	private boolean matchingModifier(int targetModifiers) {
		if (modifier == GaReflectUtils.DEFAULT_MOD) {
			// 如果默认是全匹配
			return true;
		}
		return false;
	}
	
	private boolean matchingName(String targetName) {
		return name.matching(targetName);
	}
	
	private boolean matchingAnnotation(Annotation[] targetAnnotations) {
		if (annotations == null) {
			// 如果匹配器为空，则认为匹配器不匹配Annotation
			return true;
		}
		return false;
	}
	
}
