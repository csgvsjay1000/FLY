package ivg.cn.tools.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AddSecurityCheckClassAdapter extends ClassVisitor implements Opcodes{

	public AddSecurityCheckClassAdapter(ClassVisitor cv) {
		super(ASM7,cv);
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
			String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
		
		if (mv != null) {
			if (name.equals("operation")) {
				mv = new AddSecurityCheckMethodAdapter(ASM7,mv);
			}
		}
		return mv;
	}
}
