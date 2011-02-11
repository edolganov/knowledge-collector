package ru.kc.platform.command;

import java.awt.Container;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.platform.aop.AOPTool;
import ru.kc.platform.app.AppContext;
import ru.kc.platform.data.Answer;

public abstract class AbstractCommand<T> {
	
	protected Log log = LogFactory.getLog(getClass());
	protected AppContext appContext;
	protected Container ui;
	
	void init(AppContext context){
		this.appContext = context;
		ui = appContext.rootUI;
		
		new AOPTool(appContext).injectData(this);
	}
	
	public T invokeCommand() throws Exception {
		beforeInvoke();
		return invoke();
	}
	
	protected void beforeInvoke(){ /* override if need */ };
	
	protected abstract T invoke() throws Exception;
	
	
	protected <N> N invoke(AbstractCommand<N> command) throws Exception {
		return (N) appContext.commandService.invoke(command);
	}
	
	protected <N> Answer<N> invokeSafe(AbstractCommand<N> command){
		return (Answer<N>) appContext.commandService.invokeSafe(command);
	}

}
