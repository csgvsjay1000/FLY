package ivg.cn.core.util.matcher;

/**
 * 匹配器
 * */
public interface Matcher<T> {

	boolean matching(T target);
	
}
