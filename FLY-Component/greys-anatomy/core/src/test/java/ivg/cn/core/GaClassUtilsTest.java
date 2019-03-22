package ivg.cn.core;

import java.lang.management.ManagementFactory;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import ivg.cn.core.command.Command;
import ivg.cn.core.command.Commands;
import ivg.cn.core.exception.CommandException;
import ivg.cn.core.util.GaClassUtils;
import ivg.cn.core.util.matcher.ClassMatcher;
import ivg.cn.core.util.matcher.PatternMatcher;

public class GaClassUtilsTest {

	
	@Test
	public void testScan() {
		
		Set<Class<?>> classes = GaClassUtils.scanPackage(this.getClass().getClassLoader(), "ivg.cn.core.command");
//		Collection<Class<?>> classes = Commands.getInstance().getClasses();
		ClassMatcher classMatcher = new ClassMatcher(new PatternMatcher("*Command"));
		Set<Class<?>> matchedClasses = new HashSet<Class<?>>();
		for (Class<?> class1 : classes) {
			if (classMatcher.matching(class1)) {
				matchedClasses.add(class1);
			}
		}
		System.out.println("all classes count "+classes.size());
		System.out.println("matched classes count "+matchedClasses.size());
		
	}
	
	@Test
	public void testCommand() throws CommandException {
		
//		Command command = Commands.getInstance().newCommand("trace Command hello");

		String name = ManagementFactory.getRuntimeMXBean().getName();
		String pid = name.split("@")[0]; 
		System.out.println(name);
	}
	
}
