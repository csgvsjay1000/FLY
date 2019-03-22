package ivg.cn.core.command;

import java.lang.instrument.Instrumentation;

import ivg.cn.core.advisor.AdviceListener;
import ivg.cn.core.server.Session;
import ivg.cn.core.util.PointCut;

public interface Command {

	/**
	 * 获取命令动作
	 * */
	Action getAction();
	
	interface Action{
		
	}
	
	/**  类增强动作 */
	interface GetEnhancerAction extends Action{
		
		GetEnhancer action(Session session, Instrumentation inst, Printer printer) throws Throwable;
	
	}
	
	/**  行响应动作 */
	interface RowAction extends Action {
		void action(Session session, Instrumentation inst, Printer printer) throws Throwable;
	}
	
	/**
	 * 类增强
	 * */
	interface GetEnhancer{
		
		/**  获取增强功能点 */
		PointCut getPointCut();
		
		/**  获取监听器 */
		AdviceListener getAdviceListener();
		
	}
	
	/**  信息发送者 */
	interface Printer{
		
	}
	
}
