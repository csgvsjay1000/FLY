package ivg.cn.print.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ivg.cn.dbsplit.core.sql.Example;
import ivg.cn.print.dao.PrintDao;
import ivg.cn.print.dao.PrintDetailDao;
import ivg.cn.print.db.impl.PrintDaoImpl;
import ivg.cn.print.db.impl.PrintDetailDaoImpl;
import ivg.cn.print.entity.Print;
import ivg.cn.print.entity.PrintDetail;
import ivg.cn.print.status.PrintStatus;

public class PrintDaoTest {

	ClassPathXmlApplicationContext context;
	
	@Before
	public void init() {
		context = new ClassPathXmlApplicationContext("/spring/spring-content-test.xml");
		context.start();
		
	}
	
	@Test
	public void testSelect() {
		
		PrintDao printDao = context.getBean(PrintDaoImpl.class);
		
		Date date = new Date();
		Print print = new Print();
		print.setStatus(PrintStatus.BuildBill.getValue());
		List<Print> prints = printDao.selectPages(date, print, 0, 10);
		
		for (Print print2 : prints) {
			System.out.println(print2);
		}
		
	}
	
	@Test
	public void testSelectOne() {
		PrintDao printDao = context.getBean(PrintDaoImpl.class);
		Date date = new Date();
		Print print = printDao.selectOne(date, 28171807185960960L, Print.class);
		System.out.println(print);
		
		PrintDetailDao printDetailDao = context.getBean(PrintDetailDaoImpl.class);
		PrintDetail detail = printDetailDao.selectOne(date, 28173157953175553L, PrintDetail.class);
		System.out.println(detail);
		
	}
	
	@Test
	public void testBatchInsert() {
		Date date = new Date();
		
		List<PrintDetail> details = new ArrayList<>();
		for (int i = 0; i < 200; i++) {
			PrintDetail detail = new PrintDetail();
			detail.setId(System.currentTimeMillis()+i);
			detail.setNum("PB"+UUID.randomUUID());
			detail.setPrintId(1L);
			detail.setMerchantId(2L);
			detail.setCreateDate(date);
			detail.setUpdateDate(date);
			details.add(detail);
		}
		PrintDetailDao printDetailDao = context.getBean(PrintDetailDaoImpl.class);
		printDetailDao.batchInsert(date, details);
	}
	
	@Test
	public void testUpdate() {
		Date date = new Date();
		
		PrintDetail detail = new PrintDetail();
		detail.setMerchantId(30L);
		detail.setUpdateDate(date);
		
		PrintDetail condition = new PrintDetail();
		condition.setId(15410633761L);
		
		PrintDetailDao printDetailDao = context.getBean(PrintDetailDaoImpl.class);
//		printDetailDao.update(date, detail, condition);
		
		printDetailDao.updateWithId(date, detail, 1541063376821L);
	}
	
	@Test
	public void testExample() {
		
		Example example = new Example(PrintDetail.class);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date begin = null;
		Date end = new Date();
		try {
			begin = dateFormat.parse("2018-11-09");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		example.andBetween("createDate", begin, end);
		
		PrintDetailDao printDetailDao = context.getBean(PrintDetailDaoImpl.class);
		List<PrintDetail> details = printDetailDao.select(begin, example);
		System.out.println(details);
	}
	
	@Test
	public void testExample1() {
		
		Example example = new Example(PrintDetail.class);
		
		long begin = (long) (System.currentTimeMillis() - 3600*1000*48*6);
		Date beDate = new Date(begin);
		Date now = new Date();
		
		example.andEqual("status", PrintStatus.BuildBill.getValue());
		example.andBetween("createDate", beDate, now);
		
		PrintDetailDao printDetailDao = context.getBean(PrintDetailDaoImpl.class);
		List<PrintDetail> details = printDetailDao.select(beDate, example);
		System.out.println(details);
	}
	
	@Test
	public void testExampleCountSql() {
		
		Example example = new Example(PrintDetail.class);
		
		long begin = (long) (System.currentTimeMillis() - 3600*1000*48*6);
		Date beDate = new Date(begin);
		Date now = new Date();
		
		example.andEqual("status", PrintStatus.BuildBill.getValue());
		example.andBetween("createDate", beDate, now);
		
		PrintDetailDao printDetailDao = context.getBean(PrintDetailDaoImpl.class);
		Integer val = printDetailDao.count(beDate, example);
		System.out.println(val);
	}
}
