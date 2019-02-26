package ivg.cn.monitor.jmx;

import java.io.IOException;
import java.util.Set;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

public class JMXClient {

	public static void main(String[] args) throws IOException, MalformedObjectNameException {
		JMXServiceURL jmxServiceURL = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi");

		JMXConnector connector = JMXConnectorFactory.connect(jmxServiceURL, null);
		MBeanServerConnection mBeanServerConnection = connector.getMBeanServerConnection();
		
		ObjectName objectName = new ObjectName("jmxBean:name=hello");

		System.out.println("Domains:---------------");
		String domains[] = mBeanServerConnection.getDomains();
		for (String string : domains) {
			System.out.println("Domain = " + string);    
		}
		System.out.println();

		System.out.println("MBean count:---------------");
		int mbeanCount = mBeanServerConnection.getMBeanCount();
		System.out.println("MBean count = " + mbeanCount);
		System.out.println();
		
		System.out.println("process attribute:---------------"); 
		try {
			System.out.println("name = " +mBeanServerConnection.getAttribute(objectName, "Name"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
		
		System.out.println("invoke via proxy:---------------"); 
		HelloMBean helloMBeanProxy = MBeanServerInvocationHandler.newProxyInstance(mBeanServerConnection, objectName, HelloMBean.class, false);
		System.out.println(helloMBeanProxy.printHello());
		System.out.println(helloMBeanProxy.printHello("zhangshang"));
		System.out.println();
		
		System.out.println("invoke via rmi:---------------");
		try {
			System.out.println(mBeanServerConnection.invoke(objectName, "printHello", null, null));
			System.out.println(mBeanServerConnection.invoke(objectName, "printHello", new Object[]{"lisi"}, new String[] {String.class.getName()}));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
		
		System.out.println("get mbean information:---------------"); 
		try {
			MBeanInfo info = mBeanServerConnection.getMBeanInfo(objectName);
			System.out.println("Hello Class:" + info.getClassName());        
	        System.out.println("Hello Attribute:" + info.getAttributes()[0].getName());       
	        System.out.println("Hello Operation:" + info.getOperations()[0].getName()); 
		} catch (InstanceNotFoundException | IntrospectionException | ReflectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println();
		
		System.out.println("ObjectName of MBean:---------------");   
		Set<ObjectInstance> mbSet = mBeanServerConnection.queryMBeans(null, null);
		for (ObjectInstance objectInstance : mbSet) {
			System.out.println(objectInstance.getObjectName());
		}
		connector.close();
	}
	
	
}
