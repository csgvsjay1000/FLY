package ivg.cn.monitor.jmx;

public class Hello implements HelloMBean{

	private String name = "fly";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String printHello() {
		return "Hello, "+name;
	}

	@Override
	public String printHello(String who) {
		return "Hello, "+who;
	}
	
	
}
