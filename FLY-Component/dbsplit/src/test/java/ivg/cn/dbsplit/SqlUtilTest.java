package ivg.cn.dbsplit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import ivg.cn.dbsplit.core.sql.SqlRunningBean;
import ivg.cn.dbsplit.core.sql.util.SqlUtil;

public class SqlUtilTest {

	@Test
	public void testBatchInsertSql() {
		
		List<PrintDetail> details = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			PrintDetail detail = new PrintDetail();
			detail.setId(System.currentTimeMillis()+i);
			detail.setNum("PB"+UUID.randomUUID());
			details.add(detail);
		}
		
		SqlRunningBean srb = SqlUtil.generateBatchInsertSql(details, "PRINT_2018");
		System.out.println("sql: "+srb.getSql());
		System.out.println("param: "+srb.getParams());
	}
	
	@Test
	public void testUpdateSql() {
		
		PrintDetail update = new PrintDetail();
		update.setStatus(2);
		update.setAssertUnit("包");
		PrintDetail condition = new PrintDetail();
		condition.setId(1L);
		
		
		SqlRunningBean srb = SqlUtil.generateUpdateSql(update, condition, "PRINT_2018");
		System.out.println("sql: "+srb.getSql());
		System.out.println("param: "+srb.getParams());
	}
	
}
