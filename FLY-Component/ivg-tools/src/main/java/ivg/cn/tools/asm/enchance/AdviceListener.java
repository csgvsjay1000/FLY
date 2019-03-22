package ivg.cn.tools.asm.enchance;

public interface AdviceListener {

	/**
	 * 方法前置通知
	 * 
	 * @param target 目标类实体对象,如果为静态方法则为null
	 * @param args 方法参数
	 * */
	void before(ClassLoader classLoader, String className,String methodName, String methodDesc, Object target,
			Object[] args);
	
}
