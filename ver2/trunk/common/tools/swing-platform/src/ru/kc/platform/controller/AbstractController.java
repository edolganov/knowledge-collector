package ru.kc.platform.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.platform.app.AppContext;

public abstract class AbstractController<T> {
	
	protected Log log = LogFactory.getLog(getClass());
	protected AppContext appContext;
	protected T ui;
	
	public void init(AppContext appContext, T ui){
		setAppContext(appContext);
		this.ui = ui;
		beforeInit();
		init();
	}
	
	protected void beforeInit(){ /* override if need */ };
	
	protected abstract void init();



	private void setAppContext(AppContext appContext) {
		this.appContext = appContext;
		//appContext.getEventManager().addObjectMethodListeners(this);
	}

}