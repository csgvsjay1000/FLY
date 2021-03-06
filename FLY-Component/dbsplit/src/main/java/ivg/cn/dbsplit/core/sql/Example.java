package ivg.cn.dbsplit.core.sql;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ivg.cn.dbsplit.RowBounds;
import ivg.cn.dbsplit.core.sql.util.OrmUtil;

@SuppressWarnings("serial")
public class Example implements Serializable{

	StringBuilder sb = new StringBuilder();
	List<Object> params = new ArrayList<Object>();
	List<String> selectProperties;
	boolean hadAnd = false;
	int condition = 0;
	
	Class<?> clazz;
	
	public Example(Class<?> clazz) {
		this.clazz = clazz;
	}
	
	public Example makeSelect(String... properties) {
		if (selectProperties == null) {
			selectProperties = new ArrayList<String>(properties.length);
		}
		selectProperties.addAll(Arrays.asList(properties));
		return this;
	}
	
	public Example andBetween(String property, Object begin, Object end) {
		if (condition >0) {
			sb.append(" and ");
		}
		sb.append(OrmUtil.javaClassName2DbTableName(property));
		sb.append(" between ? and ? ");
		params.add(begin);
		params.add(end);
		condition++;
		return this;
	}
	
	public Example andEqual(String property, Object value) {
		if (condition >0) {
			sb.append(" and ");
		}
		sb.append(OrmUtil.javaClassName2DbTableName(property));
		sb.append(" =? ");
		params.add(value);
		condition++;
		return this;
	}
	
	public Example andNotEqual(String property, Object value) {
		if (condition >0) {
			sb.append(" and ");
		}
		sb.append(OrmUtil.javaClassName2DbTableName(property));
		sb.append(" !=? ");
		params.add(value);
		condition++;
		return this;
	}
	
	public Example addRowBounds(RowBounds rowBounds) {
		sb.append(" limit ?,? ");
		params.add(rowBounds.getOffset());
		params.add(rowBounds.getLimit());
		return this;
	}
	
	public SqlRunningBean generateSql(DatabaseTableVO vo) {
		
		sb.insert(0, " FROM "+ vo.getPhysicalTableName() +" WHERE ");
		
		if (selectProperties == null) {
			sb.insert(0, "SELECT * ");
		}else {
			StringBuilder sbb = new StringBuilder("SELECT ");
			int i = 0;
			for(String p : selectProperties){
				if (i>0) {
					sbb.append(",");
				}
				sbb.append(p);
				i++;
			}
			sb.insert(0, sbb);
		}
		return new SqlRunningBean(sb.toString(), params.toArray());
	}
	
	
	public SqlRunningBean countSql(DatabaseTableVO vo) {
		if (sb.length() == 0) {
			sb.insert(0, " select count(*) FROM "+ vo.getPhysicalTableName());
		}else {
			sb.insert(0, " select count(*) FROM "+ vo.getPhysicalTableName() +" WHERE ");
		}
		
		return new SqlRunningBean(sb.toString(), params.toArray());
	}
	
	public Class<?> getClazz() {
		return clazz;
	}
}
