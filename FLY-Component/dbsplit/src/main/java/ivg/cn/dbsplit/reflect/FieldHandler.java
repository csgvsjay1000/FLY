package ivg.cn.dbsplit.reflect;

import java.lang.reflect.Field;

public interface FieldHandler {

	void handle(int index, Field field, Object value);
	
}
