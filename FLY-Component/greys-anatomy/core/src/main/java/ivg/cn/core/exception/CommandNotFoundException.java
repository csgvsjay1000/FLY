package ivg.cn.core.exception;

public class CommandNotFoundException extends CommandException{

	private static final long serialVersionUID = 1L;

	public CommandNotFoundException(String command) {
		super(command);
	}

}
