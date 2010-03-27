package ru.dolganov.tool.knowledge.collector.command;

import ru.dolganov.tool.knowledge.collector.ui.ExceptionHandler;

public class CommandService {
	
	private static final CommandService inst = new CommandService();

	public static void invoke(Command c){
		inst.invokeCommand(c);
	}
	
	
	private CommandService() {
		super();
	}
	
	private void invokeCommand(Command c){
		try {
			c.setCommandService(this);
			c.doAction();
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}
	}


	void invokeNext(Command a, Command b) {
		try {
			b.doAction();
			//todo
			//a.revertAction();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
}
