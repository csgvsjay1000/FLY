package ivg.cn.commodity.db;

import java.util.Calendar;
import java.util.Date;

import ivg.cn.dbsplit.core.SplitStrategy;

public class YearSplitStrategy implements SplitStrategy{

	private int portNum;
	private int dbNum;
	private int tableNum;
	private int beginYear;
	
	@Override
	public <K> int getNodeNo(K splitKey) {
		int currentY = getTableNo(splitKey);
		int num = currentY - beginYear;
		return num/tableNum*dbNum;
	}

	@Override
	public <K> int getDbNo(K splitKey) {
		int currentY = getTableNo(splitKey);
		int num = currentY - beginYear;
		return num/tableNum;
		
	}

	@Override
	public <K> int getTableNo(K splitKey) {
		if (!(splitKey instanceof Date)) {
			throw new IllegalArgumentException("splitKey must be instance of java.util.Date.");
		}
		Date date = (Date) splitKey;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	public int getPortNum() {
		return portNum;
	}

	public void setPortNum(int portNum) {
		this.portNum = portNum;
	}

	public int getDbNum() {
		return dbNum;
	}

	public void setDbNum(int dbNum) {
		this.dbNum = dbNum;
	}

	public int getTableNum() {
		return tableNum;
	}

	public void setTableNum(int tableNum) {
		this.tableNum = tableNum;
	}

	public int getBeginYear() {
		return beginYear;
	}

	public void setBeginYear(int beginYear) {
		this.beginYear = beginYear;
	}

}
