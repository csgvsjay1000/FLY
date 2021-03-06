package ivg.cn.monitor.redis;

public interface RedisDbConst {

	/**
	 * <pre>
	 * redis 实例状态
	 * 1-已上线,2-已下线, 4-已停用, 8-已废弃
	 * 16-不能使用
	 * */
	public interface ItemStatus {
		int Online = 1<<0;
		int Offline = 1<<1;
		int Stoped = 1<<2;
		int Discard = 1<<3;
		int Unnormal = 1<<4;
	}
	
	/**
	 * <pre>
	 * redis 实例状态
	 * 1-已上线,2-已下线, 4-已停用, 8-已废弃
	 * 16-不能使用
	 * */
	public interface MasterStatus {
		String OK = "OK";
		String NOT_OK = "NOT_OK";
		String Normal = "Normal";
		String Online = "Online";
		String Offline = "Offline";
	}
	
}
