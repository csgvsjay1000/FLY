package ivg.cn.dbsplit.core.sql.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.util.StringUtils;

import ivg.cn.dbsplit.core.sql.DatabaseTableVO;
import ivg.cn.dbsplit.core.sql.SqlRunningBean;
import ivg.cn.dbsplit.excep.SplitSqlException;
import ivg.cn.dbsplit.reflect.FieldHandler;
import ivg.cn.dbsplit.reflect.FieldVisitor;

public class SqlUtil {

	public static <T> SqlRunningBean generateBatchInsertSql(List<T> beans, String dbTableName) {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into ");
		
		// 1、获取表名
		sb.append(dbTableName);
		sb.append("(");
		
		List<Object> params = new LinkedList<Object>();
		
		new FieldVisitor<T>(beans.get(0)).visit(new FieldHandler() {
			
			@Override
			public void handle(int index, Field field, Object value) {
				if (index != 0) {
					sb.append(",");
				}
				// java属性名转字段名
				sb.append(OrmUtil.javaFieldName2DbFieldName(field.getName()));
			}
		});
		sb.append(") values ");
		int i = 0;
		for(T b : beans){
			if (i > 0) {
				sb.append(",");
			}
			i++;
			sb.append("(");
			new FieldVisitor<T>(b).visit(new FieldHandler() {
				
				@Override
				public void handle(int index, Field field, Object value) {
					if (index != 0) {
						sb.append(",");
					}
					// java属性名转字段名
					sb.append("?");
					
					if (value instanceof Enum) {
						value = ((Enum<?>) value).ordinal();
					}
					params.add(value);
				}
			});
			sb.append(")");
		}
		
		return new SqlRunningBean(sb.toString(), params.toArray());
	}
	
	public static <T> SqlRunningBean generateInsertSql(T bean, String databasePrefix, 
			String tablePrefix, int databaseIndex, int tableIndex, DatabaseTableVO tableVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into ");
		
		// 1、获取表名
		if (tableVO.isSplit()) {
			sb.append(getQualifiedTableName(databasePrefix, tablePrefix, databaseIndex, tableIndex));
		}else {
			// 非分片表
			sb.append(getQualifiedTableName(databasePrefix, tablePrefix));
		}
		sb.append("(");
		
		// 2、获取字段名
		List<Object> params = new LinkedList<Object>();
		new FieldVisitor<T>(bean).visit(new FieldHandler() {
			
			@Override
			public void handle(int index, Field field, Object value) {
				if (index != 0) {
					sb.append(",");
				}
				// java属性名转字段名
				sb.append(OrmUtil.javaFieldName2DbFieldName(field.getName()));
				
				if (value instanceof Enum) {
					value = ((Enum<?>) value).ordinal();
				}
				params.add(value);
			}
		});
		
		sb.append(") values (");
		sb.append(OrmUtil.generateParamPlaceholders(params.size()));
		sb.append(")");
		
		return new SqlRunningBean(sb.toString(), params.toArray());
	}
	
	public static <T> SqlRunningBean generateDeleteSql(long id,Class<T> clazz, String databasePrefix, 
			String tablePrefix, int databaseIndex, int tableIndex) {
		StringBuilder sb = new StringBuilder();
		sb.append("delete from ");
		
		sb.append(getQualifiedTableName(databasePrefix, tablePrefix, databaseIndex, tableIndex));
		sb.append(" where ID=? ");
		
		List<Object> params = new LinkedList<Object>();
		params.add(id);
		return new SqlRunningBean(sb.toString(), params.toArray());
	}
	
	public static <T> SqlRunningBean generateSearchSql(T bean, String tableName, int offset, int limit) {
		return generateSearchSql(bean, null, null, null,tableName,offset,limit);
	}
	
	public static <T> SqlRunningBean generateSelectSqlWithId(long id, String dbPrefix, String tablePrefix, 
			int dbIndex, int tableIndex) {
		StringBuilder sb = new StringBuilder();

		sb.append("select * from ");
		
		sb.append(getQualifiedTableName(dbPrefix, tablePrefix, dbIndex, tableIndex));
		sb.append("where id=?");
		List<Object> params = new ArrayList<Object>(1);
		params.add(id);
		return new SqlRunningBean(sb.toString(), params.toArray());
	}
	
	public static <T> SqlRunningBean generateSearchSql(T bean, String name, Object valueFrom, Object valueTo,
			String tableName, int offset, int limit) {
		StringBuilder sb = new StringBuilder();
		sb.append("select * from ");
		
		sb.append(tableName);
		
		List<Object> params = new LinkedList<Object>();
		if (bean != null) {
			sb.append(" where ");
			// 2、获取字段名
			new FieldVisitor<T>(bean).visit(new FieldHandler() {
				
				@Override
				public void handle(int index, Field field, Object value) {
					if (index != 0) {
						sb.append(" and ");
					}
					// java属性名转字段名
					sb.append(OrmUtil.javaFieldName2DbFieldName(field.getName())).append("=? ");
					
					if (value instanceof Enum) {
						value = ((Enum<?>) value).ordinal();
					}
					params.add(value);
				}
			});
		}
		
		if (limit > 0) {
			sb.append(" limit ?,?");
			params.add(offset);
			params.add(limit);
		}
		
		return new SqlRunningBean(sb.toString(), params.toArray());
	}
	
	public static <T> SqlRunningBean generateUpdateSqlWithId(T bean, long id, String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append("update ");
		
		sb.append(tableName);
		sb.append(" set ");

		List<Object> params = new LinkedList<Object>();
		new FieldVisitor<T>(bean).visit(new FieldHandler() {
			
			@Override
			public void handle(int index, Field field, Object value) {
				if (index != 0) {
					sb.append(",");
				}
				// java属性名转字段名
				sb.append(OrmUtil.javaFieldName2DbFieldName(field.getName())).append("=? ");
				
				if (value instanceof Enum) {
					value = ((Enum<?>) value).ordinal();
				}
				params.add(value);
			}
		});
		sb.append(" where ID=?");
		params.add(id);
		return new SqlRunningBean(sb.toString(), params.toArray());
	}
	
	public static <T> SqlRunningBean generateUpdateSql(T bean, T condition, String tableName) {
		StringBuilder sb = new StringBuilder();
		sb.append("update ");
		
		sb.append(tableName);
		sb.append(" set ");

		List<Object> params = new LinkedList<Object>();
		new FieldVisitor<T>(bean).visit(new FieldHandler() {
			
			@Override
			public void handle(int index, Field field, Object value) {
				if (index != 0) {
					sb.append(",");
				}
				// java属性名转字段名
				sb.append(OrmUtil.javaFieldName2DbFieldName(field.getName())).append("=? ");
				
				if (value instanceof Enum) {
					value = ((Enum<?>) value).ordinal();
				}
				params.add(value);
			}
		});
		sb.append(" where ");
		new FieldVisitor<T>(condition).visit(new FieldHandler() {
			
			@Override
			public void handle(int index, Field field, Object value) {
				if (index != 0) {
					sb.append(" and ");
				}
				// java属性名转字段名
				sb.append(OrmUtil.javaFieldName2DbFieldName(field.getName())).append("=? ");
				
				if (value instanceof Enum) {
					value = ((Enum<?>) value).ordinal();
				}
				params.add(value);
			}
		});
		
		return new SqlRunningBean(sb.toString(), params.toArray());
	}

	/**
	 * 获取合适的表名
	 * */
	public static String getQualifiedTableName(String databasePrefix, 
			String tablePrefix, int databaseIndex, int tableIndex ) {
		StringBuilder sb = new StringBuilder();
		
		if (!StringUtils.isEmpty(databasePrefix)) {
			sb.append(databasePrefix);
		}
		if (databaseIndex != -1) {
			sb.append("_").append(databaseIndex).append(".");
		}
		if (!StringUtils.isEmpty(tablePrefix)) {
			sb.append(tablePrefix);
		}
		if (tableIndex != -1) {
			sb.append("_").append(tableIndex).append(" ");
		}
		return sb.toString();
	}
	
	/**
	 * 获取不分片的表名
	 * */
	public static String getQualifiedTableName(String databasePrefix, 
			String tablePrefix ) {
		StringBuilder sb = new StringBuilder();
		
		if (!StringUtils.isEmpty(databasePrefix)) {
			sb.append(databasePrefix).append(".");
		}
		if (!StringUtils.isEmpty(tablePrefix)) {
			sb.append(tablePrefix);
		}
		return sb.toString();
	}
}
