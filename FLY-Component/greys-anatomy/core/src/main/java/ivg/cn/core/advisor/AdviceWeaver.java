package ivg.cn.core.advisor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class AdviceWeaver extends ClassVisitor implements Opcodes{

	public AdviceWeaver(int api) {
		super(api);
		// TODO Auto-generated constructor stub
	}

}
