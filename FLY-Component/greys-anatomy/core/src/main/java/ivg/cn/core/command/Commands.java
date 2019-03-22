package ivg.cn.core.command;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

import ivg.cn.core.command.annotation.Cmd;
import ivg.cn.core.command.annotation.IndexArg;
import ivg.cn.core.exception.CommandException;
import ivg.cn.core.exception.CommandInitializationException;
import ivg.cn.core.exception.CommandNotFoundException;
import ivg.cn.core.util.GaClassUtils;
import ivg.cn.core.util.GaReflectUtils;
import ivg.cn.core.util.GaStringUtils;
import ivg.cn.core.util.LogUtil;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class Commands {
	private final Logger logger = LogUtil.getLogger();

	private final Map<String/**cmdName*/, Class<?>> commands = new HashMap<String, Class<?>>();
	
	public Commands() {
		logger.info("Commands.ClassLoader = "+this.getClass().getClassLoader());
		for(Class<?> clazz : GaClassUtils.scanPackage(Commands.class.getClassLoader(), "ivg.cn.core.command")){
			if (!Command.class.isAssignableFrom(clazz) || Modifier.isAbstract(clazz.getModifiers())) {
				// 如果没有实现Command类，或clazz是抽象类，则过滤
				continue;
			}
			if (clazz.isAnnotationPresent(Cmd.class)) {
				// 如果类包含cmd
				Cmd cmd = clazz.getAnnotation(Cmd.class);
				commands.put(cmd.name(), clazz);
				logger.info(clazz+": "+clazz.getClassLoader());
			}
		}
	}
	
	private static Commands instance = new Commands();
	public static Commands getInstance() {
		return instance;
	}
	
	public Collection<Class<?>> getClasses() {
		return commands.values();
	}
	
	public Command newCommand(String line) throws CommandException{
		final String[] splitOfLine = GaStringUtils.splitForArgument(line);
		String cmdName = splitOfLine[0];
		Class<?> clazz = getInstance().commands.get(cmdName);
		if (clazz == null) {
			// 该命令不支持
			throw new CommandNotFoundException(cmdName);
		}
		Command command;
		try {
			command = (Command) clazz.newInstance();
		} catch (Throwable e) {
			throw new CommandInitializationException(cmdName);
		}
		// 命令实例参数赋值
		try {
			OptionSet opt = getOptionParser(clazz).parse(splitOfLine);
			for(Field field : clazz.getDeclaredFields()){
				
				// 处理IndexArg参数
				if (field.isAnnotationPresent(IndexArg.class)) {
					IndexArg indexArg = field.getAnnotation(IndexArg.class);
					final int index = indexArg.index() + 1;
					
					if (indexArg.isRequired() && opt.nonOptionArguments().size() <= index) {
						throw new IllegalArgumentException(indexArg.name()+" argument was missing.");
					}
					
					if (opt.nonOptionArguments().size() > index) {
						// 非名称参数的参数个数，即位置参数个数
						try {
							GaReflectUtils.setValue(field, opt.nonOptionArguments().get(index), command);
						} catch (Exception e) {
							throw new CommandInitializationException(cmdName,e);
						}
					}
					
				}
			}
			
		} catch (Throwable e) {
			throw new CommandException(cmdName, e);
		}
		
		return command;
	}
	
	/**
	 * 获取类的属性，封装成OptionParser
	 * */
	private static OptionParser getOptionParser(Class<?> clazz) {
		final OptionParser parser = new OptionParser();
		// 1、遍历类声明的属性
		for(Field field : clazz.getDeclaredFields()){
			// 2、
//			if (field.isAnnotationPresent(annotationClass)) {
//				
//			}
		}
		
		return parser;
	}
	
}
