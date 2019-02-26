package ivg.cn.print.status;

public enum PrintStatus {

	BuildBill(0,"未打印"),
	Cancel(1,"已取消"),
	Generating(2,"打印中"),
	GeneratFinshed(9,"打印完成")
	;
	
	int value;
	
	String name;
	
	private PrintStatus(int value,String name) {
		this.value = value;
		this.name = name;
	}
	
	public int getValue() {
		return value;
	}
	
}
