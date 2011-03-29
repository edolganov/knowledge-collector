package ru.kc.platform.command;

import java.awt.Container;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.platform.app.AppContext;
import ru.kc.platform.data.Answer;
import ru.kc.platform.domain.DomainMember;
import ru.kc.platform.domain.RootDomainMember;
import ru.kc.platform.event.Event;
import ru.kc.platform.event.Request;
import ru.kc.platform.reflection.ReflectionTool;

public abstract class AbstractCommand<T> {
	
	protected Log log = LogFactory.getLog(getClass());
	protected AppContext appContext;
	protected Container ui;
	protected DomainMember domainMember;
	
	
	public AbstractCommand() {
		this(null);
	}

	public AbstractCommand(DomainMember domainMember) {
		super();
		if(domainMember == null){
			domainMember = new RootDomainMember(this);
		}
		this.domainMember = domainMember;
	}



	void init(AppContext context){
		this.appContext = context;
		ui = appContext.rootUI;
		
		new ReflectionTool(appContext, ui).injectData(this);
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
	
	protected void fireEvent(Event event){
		appContext.eventManager.fireEventInEDT(domainMember, event);
	}
	
	protected <N> Answer<N> invokeSafe(Request<N> request){
		return (Answer<N>)appContext.eventManager.fireSaveRequestInEDT(domainMember, request);
	}

}
