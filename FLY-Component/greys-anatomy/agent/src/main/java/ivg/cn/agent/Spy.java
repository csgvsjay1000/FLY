package ivg.cn.agent;

import java.lang.reflect.Method;

/**
 * 间谍类，藏匿在各个ClassLoader内
 * */
public class Spy {

	/**  代理重置类 */
	public static volatile Method AGENT_RESET_METHOD;
	
	public static void initForAgentLauncher(Method agentResetMethod) {
		AGENT_RESET_METHOD = agentResetMethod;
	}
	
	public static void clean() {
        AGENT_RESET_METHOD = null;
    }
}
