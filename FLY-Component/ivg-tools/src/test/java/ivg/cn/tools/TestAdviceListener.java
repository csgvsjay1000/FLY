package ivg.cn.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ivg.cn.tools.asm.enchance.AdviceListener;

public class TestAdviceListener implements AdviceListener{

	Logger logger = LoggerFactory.getLogger(TestAdviceListener.class);

	
	@Override
	public void before(ClassLoader classLoader, String className, String methodName, String methodDesc, Object target,
			Object[] args) {
		logger.info("current method name:" + methodName);
	}

}
