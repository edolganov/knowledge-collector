package ru.kc.platform.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.platform.app.AppContext;
import ru.kc.platform.data.Answer;

public class CommandService {
	
	private static Log log = LogFactory.getLog(CommandService.class);
	
	AppContext context;
	
	public void init(AppContext context){
		this.context = context;
	}
	
	public <T> T invoke(AbstractCommand<T> command) throws Exception{
		command.init(context);
		return (T)command.invokeCommand();
	}
	
	public <T> Answer<T> invokeSafe(AbstractCommand<T> command){
		try{
			T result = (T)invoke(command);
			return new Answer<T>(result, false, null);
		}catch (Exception e) {
			log.error("error while invoke "+command, e);
			return new Answer<T>(null, true, e);
		}
	}
	
	

}
