package ivg.cn.monitor.jmx;

public interface HelloMBean {

	public String getName();

	public void setName(String name);
	
	String printHello();
	
	String printHello(String who);
	
}
