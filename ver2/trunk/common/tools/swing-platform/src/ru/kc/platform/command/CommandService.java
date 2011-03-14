package ru.kc.platform.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.platform.app.AppContext;
import ru.kc.platform.data.Answer;

public class CommandService {
	
	private static Log log = LogFactory.getLog(CommandService.class);
	
	AppContext context;
	
	public void setContext(AppContext context){
		this.context = context;
	}
	
	public <T> T invoke(AbstractCommand<T> command) throws Exception{
		command.init(context);
		return (T)command.invokeCommand();
	}
	
	public <T> Answer<T> invokeSafe(AbstractCommand<T> command){
		try{
			T result = (T)invoke(command);
			return new Answer<T>(result);
		}catch (Exception e) {
			log.error("error while invoke "+command, e);
			return new Answer<T>(e);
		}
	}
	
	

}
