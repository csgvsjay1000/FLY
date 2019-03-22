package ivg.cn.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AopAgentTest {

	public static void premain(String args, Instrumentation inst) {
		
	}
	
	
	class AopAgentTransformer implements ClassFileTransformer{

		@Override
		public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
				ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
			
			return classfileBuffer;
		}
		
	}
	
	class ClassAdapter extends ClassVisitor implements Opcodes{
		
		private String owner;
		
		public ClassAdapter() {
			super(ASM7);
		}
		
		@Override
		public void visit(int version, int access, String name, String signature, String superName,
				String[] interfaces) {
			super.visit(version, access, name, signature, superName, interfaces);
			this.owner = name;
		}
		
	}
	
	class MethodAdapter extends MethodVisitor implements Opcodes{
		public MethodAdapter() {
			super(ASM7);
		}
		
		@Override
		public void visitCode() {
			super.visitCode();
			mv.visitFieldInsn(GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");   // 调用System.err.println();
			
			
			
		}
		
	}
	
	
	
}
