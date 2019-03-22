package ivg.cn.tools.asm.enchance;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;


/**
 * 增强器
 * */
public class Enchancer {

	private Class<?> targetClass;  // 增强目标
	
	private AdviceListener adviceListener;
	
	private int adviceId;
	
	private static AtomicInteger ID_GENERATOR = new AtomicInteger(10);
	
	private String transferName;
	
	public void setAdviceListener(AdviceListener adviceListener) {
		this.adviceListener = adviceListener;
		this.adviceId = ID_GENERATOR.getAndIncrement();
	}
	
	public void setTargetClass(Class<?> targetClass) {
		this.targetClass = targetClass;
	}
	
	/**  将类增强 
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException */
	public Object enchance() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		ClassReader cr = new ClassReader(targetClass.getName());
		ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
		cr.accept(new AdviceWeaver(adviceId, adviceListener, targetClass, cw), ClassReader.EXPAND_FRAMES);
		
		byte[] data = cw.toByteArray();
		
        EnchanceClassLoader classLoader = new EnchanceClassLoader();
		Class<?> clazz = classLoader.defineClass(targetClass.getName(), data);
		return clazz.newInstance();
	}
	
	public String genTransferClassName() {
        if (transferName == null) {
            this.transferName = this.targetClass.getName().replace("/", ".");
        }

        return this.transferName;
    }
	
	class EnchanceClassLoader extends ClassLoader{
		
		public Class<?> defineClass(String name,byte[] data) {
			return this.defineClass(name, data, 0, data.length);
		}
		
	}
	
}
