package ivg.cn.monitor.jmx;

import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

public class HelloAgent {

	public static void main(String[] args) {
		MBeanServer mBeanServer =  ManagementFactory.getPlatformMBeanServer();
		
		try {
			ObjectName objectName = new ObjectName("jmxBean:name=hello");
			Hello hello = new Hello();
//			hello.setName("fly1");
			mBeanServer.registerMBean(hello, objectName);
			
			
//			mBeanServer.registerMBean(object, name)
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Registry registry = null;
		try {
			registry = LocateRegistry.createRegistry(1099);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			JMXServiceURL jmxServiceURL = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:1099/jmxrmi");
			JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(jmxServiceURL, null, mBeanServer);
			cs.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}	
}
