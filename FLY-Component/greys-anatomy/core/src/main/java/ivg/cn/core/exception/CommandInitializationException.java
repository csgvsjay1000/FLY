package ivg.cn.core.exception;

public class CommandInitializationException extends CommandException{

	private static final long serialVersionUID = 1L;

	public CommandInitializationException(String command) {
		super(command);
	}
	
	public CommandInitializationException(String command, Throwable e) {
		super(command,e);
	}

}
