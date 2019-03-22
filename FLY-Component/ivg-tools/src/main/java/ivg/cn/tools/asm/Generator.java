package ivg.cn.tools.asm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public class Generator {

	public static void main(String[] args) throws IOException {
		ClassReader classReader = new ClassReader("ivg.cn.tools.asm.Account");
		
		ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		
		ClassVisitor classVisitor = new AddSecurityCheckClassAdapter(classWriter);
		classReader.accept(classVisitor, ClassReader.SKIP_DEBUG);
		
		byte[] data = classWriter.toByteArray();
		File file = new File("Account.class"); 
        FileOutputStream fout = new FileOutputStream(file); 
        fout.write(data); 
        fout.close();
		
	}
	
}
