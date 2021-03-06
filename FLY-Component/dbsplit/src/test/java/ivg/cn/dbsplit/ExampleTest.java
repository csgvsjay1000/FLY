package ivg.cn.dbsplit;

import org.junit.Test;

import ivg.cn.dbsplit.core.sql.DatabaseTableVO;
import ivg.cn.dbsplit.core.sql.Example;
import ivg.cn.dbsplit.core.sql.SqlRunningBean;

public class ExampleTest {

	@Test
	public void testMakeSql() {
		
		Example example = new Example(PrintDetail.class);
		
		example.makeSelect("ID","printId");
		example.andBetween("printId", 1, 2);
		example.addRowBounds(new RowBounds(1, 20));
		
		DatabaseTableVO vo = new DatabaseTableVO("print", "print", 0, 0, null);
		SqlRunningBean srb = example.generateSql(vo);
		
		System.out.println(srb.getSql());
		for(Object p: srb.getParams()){
			System.out.print(p+",");
		}
		
	}
	
	@Test
	public void testMakeCountSql() {
		
		Example example = new Example(PrintDetail.class);
		
//		example.andBetween("printId", 1, 2);
		
		DatabaseTableVO vo = new DatabaseTableVO("print", "print", 0, 0, null);
		SqlRunningBean srb = example.countSql(vo);
		
		System.out.println(srb.getSql());
		for(Object p: srb.getParams()){
			System.out.print(p+",");
		}
		
	}
	
}
