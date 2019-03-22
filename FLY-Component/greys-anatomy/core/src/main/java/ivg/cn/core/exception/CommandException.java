package ivg.cn.core.exception;

public class CommandException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6951555284848120132L;
	
	private final String command;
	
	public CommandException(String command) {
		this.command = command;
	}
	
	public CommandException(String command, Throwable e) {
		super(e);
		this.command = command;
	}
	
	public String getCommand() {
		return command;
	}

}
