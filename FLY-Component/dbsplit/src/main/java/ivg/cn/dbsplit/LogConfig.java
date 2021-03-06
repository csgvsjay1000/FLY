package ivg.cn.dbsplit;

public class LogConfig {

	public static final String LOG_NAME = "dbsplit";
	private static LogConfig logConfig = new LogConfig();
	
	private static final int LOG_ERROR = 0x01;
	private static final int LOG_WARN = 0x01 << 1;
	private static final int LOG_INFO = 0x01 << 2;
	private static final int LOG_DEBUG = 0x01 << 3;
	
	private static final int LEV_ERROR = LOG_ERROR;
	private static final int LEV_WARN = LOG_WARN | LOG_ERROR;
	private static final int LEV_INFO = LOG_INFO | LOG_WARN | LOG_ERROR;
	private static final int LEV_DEBUG = LOG_DEBUG | LOG_INFO | LOG_WARN | LOG_ERROR;
	
	private volatile int level = LEV_DEBUG;
	
	public static LogConfig getInstance() {
		return logConfig;
	}
	
	private LogConfig() {
		// TODO Auto-generated constructor stub
	}
	
	public void makeDebug() {
		this.level = LEV_DEBUG;
	}
	
	public void makeInfo() {
		this.level = LEV_INFO;
	}
	
	public void makeWarn() {
		this.level = LEV_WARN;
	}
	public void makeError() {
		this.level = LEV_ERROR;
	}
	
	public boolean isEnableDebug() {
		return (this.level & LOG_DEBUG) !=0;
	}
	
	public boolean isEnableInfo() {
		return (this.level & LOG_INFO) !=0;
	}

	public boolean isEnableWarn() {
		return (this.level & LOG_WARN) !=0;
	}
	
	public boolean isEnableError() {
		return (this.level & LOG_ERROR) !=0;
	}
}
