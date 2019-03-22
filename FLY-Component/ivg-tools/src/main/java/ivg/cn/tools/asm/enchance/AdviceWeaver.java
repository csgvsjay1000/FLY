package ivg.cn.tools.asm.enchance;

import java.util.concurrent.ConcurrentHashMap;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.JSRInlinerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdviceWeaver extends ClassVisitor implements Opcodes, AsmMethods{

	Logger logger = LoggerFactory.getLogger(AdviceWeaver.class);
	
	private static final Type ADVICE_WEAVER_TYPE = Type.getType(AdviceWeaver.class);
	private static final ConcurrentHashMap<Integer, AdviceListener> LISTENER_MAP = new ConcurrentHashMap<>();
	
	private int adviceId;
	
	public AdviceWeaver(int adviceId, AdviceListener adviceListener, Class<?> targetClass, ClassVisitor cv) {
		super(ASM7, cv);
		this.adviceId = adviceId;
		LISTENER_MAP.putIfAbsent(adviceId, adviceListener);
	}
	
	/**
	 * @param target 增强目标类的实例对象，若增强的方法是静态方法，则为null
	 * */
	public static void methodOnBegin(int adviceId, Object target, Object[] args) {
        AdviceListener listener = LISTENER_MAP.get(adviceId);
        
        listener.before(null, null, null, null, target, args);
        
	}
	
	public static void methoOnReturn(Object returnVal) {
		
	}
	
	private static void methoOnEnd(boolean isThrowing, Object returnTarget) {
		
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
			String[] exceptions) {
//		System.out.println(String.format("Thread [%s] ,access [%d], name [%s], descriptor [%s], signature [%s], exceptions [%s] ", 
//				Thread.currentThread().getName(),access,
//				name,descriptor,signature,exceptions));
		MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
		
		// 1、过滤掉抽象、内部初始化<init>方法
		if (isIgnore(mv, access, name)) {
			return mv;
		}
		
		AdviceAdapter adapter = new AdviceAdapter(ASM7,new JSRInlinerAdapter(mv, access, name, descriptor, signature, exceptions) ,access,name,descriptor) {
			
			@Override
			protected void onMethodEnter() {
				push(adviceId);
				
				// push target
				pushTarget();
				
				// push args
				loadArgArray();
				invokeStatic(ADVICE_WEAVER_TYPE, AdviceWeaver_methodOnBegin);
				
			}
			
			@Override
			protected void onMethodExit(int opcode) {
				// 后置增强, 若不是异常结束
				if (ATHROW != opcode) {
					// 加载正常返回值
					loadReturn(opcode);
					invokeStatic(ADVICE_WEAVER_TYPE, AdviceWeaver_methoOnReturn);
				}
			}
			
			@Override
			public void visitMaxs(int maxStack, int maxLocals) {
				// 该方法在每个方法最后调用一次,是增加字节码，不是在运行时候访问

				super.visitMaxs(maxStack, maxLocals);
			}
			
			private void pushTarget() {
				// 如果是静态方法，则push null
				if (isStatic()) {
					push((Type)null);
				}else {
					loadThis();
				}
			}
			
			private boolean isStatic() {
				return (ACC_STATIC & methodAccess) == ACC_STATIC; 
			}
			
			private void loadReturn(int opcode) {
				switch (opcode) {
				case RETURN:
					// 没有返回值
					push((Type)null);
					break;
				case ARETURN:
					// 返回一个对象
					dup();
					break;
				

				default:
					break;
				}
			}
			
		};
		
		return adapter;
	}
	
	/**  忽略抽象方法，init<>方法 */
	private boolean isIgnore(MethodVisitor mv, int access, String name) {
		return mv == null || 
				isAbstract(access) || 
				isFinalMethod(access) || 
				"<clinit>".equals(name) ||
				"<init>".equals(name);
	}
	
	private boolean isAbstract(int access) {
		return (ACC_ABSTRACT & access) == ACC_ABSTRACT;
	}
	
	private boolean isFinalMethod(int access) {
		return (ACC_FINAL & access) == ACC_FINAL;
	}
	
}
