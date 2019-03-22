package ivg.cn.tools.asm;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AddSecurityCheckMethodAdapter extends MethodVisitor implements Opcodes{

	public AddSecurityCheckMethodAdapter(int api,MethodVisitor mv) {
		super(api,mv);
	}
	
	@Override
	public void visitCode() {
		super.visitCode();
		visitMethodInsn(Opcodes.INVOKESTATIC, "ivg/cn/tools/asm/SecurityChecker", "checkSecurity", "()V", false);
	}
	
	@Override
	public void visitEnd() {
		super.visitEnd();
	}

}
