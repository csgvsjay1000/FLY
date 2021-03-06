package ivg.cn.dbsplit.core.sql.util;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Date;

import com.mysql.jdbc.ResultSetMetaData;

import ivg.cn.dbsplit.reflect.ReflectionUtil;

public class OrmUtil {

	/**
	 * eg TestTable --> TEST_TABLE
	 * */
	public static String javaClassName2DbTableName(String name) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < name.length(); i++) {
			if (Character.isUpperCase(name.charAt(i)) && i != 0) {
				sb.append("_");
			}

			sb.append(Character.toUpperCase(name.charAt(i)));

		}
		return sb.toString();
	}
	
	/**
	 * eg testField --> TEST_FIELD
	 * */
	public static String javaFieldName2DbFieldName(String name) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < name.length(); i++) {
			if (Character.isUpperCase(name.charAt(i)) ) {
				sb.append("_");
			}

			sb.append(Character.toUpperCase(name.charAt(i)));

		}
		return sb.toString();
	}
	
	/**
	 * eg TEST_FIELD --> testField
	 * */
	public static String dbFieldName2JavaFieldName(String name) {
		StringBuilder sb = new StringBuilder();

		boolean lower = true;
		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) == '_') {
				lower = false;
				continue;
			}

			if (lower)
				sb.append(Character.toLowerCase(name.charAt(i)));
			else {
				sb.append(Character.toUpperCase(name.charAt(i)));
				lower = true;
			}

		}
		return sb.toString();
	}
	
	/**
	 * 生成 ?,?,?
	 * */
	public static String generateParamPlaceholders(int count) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < count; i++) {
			if (i != 0)
				sb.append(",");
			sb.append("?");
		}

		return sb.toString();
	}
	
	public static <T> T convertRow2Bean(ResultSet rs, Class<T> clazz) {
		try {
			T bean = clazz.newInstance();
			ResultSetMetaData  rsmd = (ResultSetMetaData) rs.getMetaData();
			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				int columnType = rsmd.getColumnType(i);
				String columnName = rsmd.getColumnName(i);
				
				// 生成set方法, 供反射使用
				String fieldName = dbFieldName2JavaFieldName(columnName);
				String setterName = ReflectionUtil.fieldName2SetterName(fieldName);  
				
				Class<? extends Object> param = null;
				Object value = null;
				switch (columnType) {
				case Types.CHAR:
					param = String.class;
					value = rs.getString(i);
					break;
				case Types.VARCHAR:
					param = String.class;
					value = rs.getString(i);
					break;
				case Types.BIGINT:
					param = Long.class;
					value = rs.getLong(i);
					break;
				case Types.SMALLINT:
					param = Integer.class;
					value = rs.getInt(i);
					break;
				case Types.INTEGER:
					param = Integer.class;
					value = rs.getInt(i);
					break;
				
				case Types.TIME:
					param = Date.class;
					value = rs.getDate(i);
					break;
				case Types.TIMESTAMP:
					param = Date.class;
					value = rs.getTimestamp(i);
					break;

				default:
					String info = bean+", columnType="+columnType+", fieldName="+fieldName;
					System.out.println(bean+", columnType="+columnType+", fieldName="+fieldName);
					throw new Exception("Db column not supported. "+info);
				}
				try {
					Method setter = clazz.getMethod(setterName, param);
					setter.invoke(bean,value);
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
				
			}
			return bean;
		} catch (Exception e) {
			throw new IllegalStateException(
					"Fail to operator on ResultSet metadata.", e);
		}
		
	}
}
