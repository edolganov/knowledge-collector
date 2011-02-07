package ru.kc.platform.command;

import ru.kc.platform.app.AppContext;

public class CommandService {
	
	public <T> T invoke(AbstractCommand<T> command, AppContext context) throws Exception{
		command.init(context);
		return (T)command.invokeCommand();
	}
	
	

}
