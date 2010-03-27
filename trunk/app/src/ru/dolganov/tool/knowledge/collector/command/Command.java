package ru.dolganov.tool.knowledge.collector.command;

import ru.dolganov.tool.knowledge.collector.App;
import ru.dolganov.tool.knowledge.collector.dao.DAO;

public abstract class Command {
	
	protected DAO dao = App.getDefault().getDao();
	private CommandService commandService;
	
	void setCommandService(CommandService commandService) {
		this.commandService = commandService;
	}

	abstract void doAction() throws Exception;
	
	void doNextAction(Command c) throws Exception {
		commandService.invokeNext(this,c);
	}

}
