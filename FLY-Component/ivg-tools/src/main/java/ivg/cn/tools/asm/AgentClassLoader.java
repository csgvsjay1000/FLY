package ivg.cn.tools.asm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class AgentClassLoader extends ClassLoader{

	String classPath;
	
	public AgentClassLoader(String agentJar) throws MalformedURLException {
		this.classPath = agentJar;
	}
	
//	@Override
//	protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
//		
//		// 先查看是否已经加载过
//		Class<?> loadedClass = findLoadedClass(name);
//		if (loadedClass != null) {
//			return loadedClass;
//		}
//		try {
//			Class<?> aClass = findClass(name);
//			if (resolve) {
//				resolveClass(aClass);
//			}
//			return aClass;
//		} catch (Exception e) {
//			return super.loadClass(name, resolve);
//		}
//		
//	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		try {
			byte [] classDate=getData(name);
			if (classDate != null) {
				return defineClass(name,classDate, 0, classDate.length);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.findClass(name);
	}
	
	private byte[] getData(String className) throws IOException{
        InputStream in = null;
        ByteArrayOutputStream out = null;
        String path=classPath + File.separatorChar +
                    className.replace('.',File.separatorChar)+".class";
        try {
            in=new FileInputStream(path);
            out=new ByteArrayOutputStream();
            byte[] buffer=new byte[2048];
            int len=0;
            while((len=in.read(buffer))!=-1){
                out.write(buffer,0,len);
            }
            return out.toByteArray();
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally{
            in.close();
            out.close();
        }
        return null;
    }

}
