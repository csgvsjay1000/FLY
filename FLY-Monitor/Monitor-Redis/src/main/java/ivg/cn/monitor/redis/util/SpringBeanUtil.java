package ivg.cn.monitor.redis.util;

import org.springframework.context.ApplicationContext;

public class SpringBeanUtil {

	private static ApplicationContext context;
	
	public static void setContext(ApplicationContext ct) {
		context = ct;
	}
	
	public static <T> T getBean(Class<T> type) {
		return context.getBean(type);
	}
	
}
