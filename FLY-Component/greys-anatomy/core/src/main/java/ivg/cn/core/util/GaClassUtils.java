package ivg.cn.core.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

import ivg.cn.core.server.GaServer;

public class GaClassUtils {
	private static final Logger logger = LogUtil.getLogger();

	public static Set<Class<?>> scanPackage(final ClassLoader targetClassLoader, final String targetPackageName) {
		
		// 第一个
		Set<Class<?>> classes = new HashSet<Class<?>>();
		
		String packageName = targetPackageName;
		String packageDirName = packageName.replace('.', '/');
		
		Enumeration<URL> dirs;
		
		try {
			dirs = targetClassLoader.getResources(packageDirName);
			logger.info("dirs="+dirs);
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				logger.info("dirs.hasMoreElements="+url);
				String protocol = url.getProtocol();  // 获取协议
				
				if ("file".equals(protocol)) {
					// 如果是文件形式, 获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					findAndAddClassesInPackageFile(targetClassLoader,packageName, filePath, true, classes);
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return classes;
	}
	
	/**
	 * 扫描目标包下类，添加到classes集合中
	 * */
	private static void findAndAddClassesInPackageFile(ClassLoader classLoader ,String packageName,String packagePath,final Boolean recursive, Set<Class<?>> classes) {
		// 获取此包的目录，建立一个文件
		File dir = new File(packagePath);
		if (!dir.exists() || !dir.isDirectory()) {
			// 如果目录不存在，或不是目录则返回
			logger.info("findAndAddClassesInPackageFile.dir={}, not exists or not a directory",dir);
			return;
		}
		logger.info("findAndAddClassesInPackageFile.dir="+dir);
		// 如果存在就获取目录下所有文件
		File[] disfiles = dir.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				// 递归子目录文件，接受.class文件
				return ((recursive && pathname.isDirectory()) || pathname.getName().endsWith(".class"));
			}
		});
		// 遍历所有文件
		for (File file : disfiles) {
			if (file.isDirectory()) {
				// 如果是目录就继续扫描
				findAndAddClassesInPackageFile(classLoader,packageName+"."+file.getName(), file.getAbsolutePath(), recursive, classes);
			}else {
				// 如果是java文件类名
				String className = file.getName().substring(0, file.getName().length()-6);
				try {
					Class<?> clazz = classLoader.loadClass(packageName+"."+className);
					logger.info(clazz+":"+clazz.getClassLoader());
					classes.add(clazz);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
}
