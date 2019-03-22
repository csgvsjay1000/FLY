package ivg.cn.tools.asm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityChecker {
	
	static Logger logger = LoggerFactory.getLogger(SecurityChecker.class);
	
	public static void checkSecurity() { 
        System.out.println("SecurityChecker.checkSecurity ..."); 
        //TODO real security check 
        
        logger.info("test scheck");
    }
}
