package ivg.cn.core.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指令位置参数
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IndexArg {

	/**  指令的位置 */
	int index();
	
	/**  参数名称 */
	String name();
	
	
	/**  是否必须 */
	boolean isRequired() default true;
	
}
