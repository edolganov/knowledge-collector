package ru.kc.platform.command;

import java.awt.Container;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.platform.aop.AOPTool;
import ru.kc.platform.app.AppContext;

public abstract class AbstractCommand<T> {
	
	protected Log log = LogFactory.getLog(getClass());
	protected AppContext appContext;
	protected Container rootUI;
	
	void init(AppContext context){
		this.appContext = context;
		rootUI = appContext.rootUI;
		
		new AOPTool(appContext).injectData(this);
	}
	
	public T invokeCommand() throws Exception {
		beforeInvoke();
		return invoke();
	}
	
	protected void beforeInvoke(){ /* override if need */ };
	
	protected abstract T invoke() throws Exception;

}
