package ivg.cn.core.advisor;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import ivg.cn.core.util.PointCut;

/**  类增强器 */
public class Enhancer implements ClassFileTransformer{

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**  对象增强 */
	public static void enhance(Instrumentation inst, int adviceId, boolean isTracing, PointCut pointCut) {
		
		
		// 构建增强器
		Enhancer enhancer = new Enhancer();
		
		try {
			// 增强完后要移除掉？
			inst.addTransformer(enhancer, true);
			
		} finally {
			inst.removeTransformer(enhancer);
		}
	}

}
