package ru.kc.platform.controller;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.platform.action.facade.AbstractActionFacade;
import ru.kc.platform.action.facade.ActionFacadeComparator;
import ru.kc.platform.action.facade.ActionService;
import ru.kc.platform.app.AppContext;
import ru.kc.platform.command.AbstractCommand;
import ru.kc.platform.data.Answer;
import ru.kc.platform.domain.DomainMember;
import ru.kc.platform.domain.DomainUtil;
import ru.kc.platform.event.Event;
import ru.kc.platform.event.Request;
import ru.kc.platform.module.Module;
import ru.kc.platform.runtimestorage.RuntimeStorage;

public abstract class AbstractController<T> implements DomainMember {
	
	protected Log log = LogFactory.getLog(getClass());
	protected T ui;
	protected AppContext appContext;
	protected RuntimeStorage runtimeStorage;
	
	private ControllersPool controllersPool;
	private ActionFacades actionFacades;
	protected ActionService actionService;
	
	void setUIObject(T ui){
		this.ui = ui;
	}
	
	void init(AppContext appContext){
		initContext(appContext);
		initActionFacades();
		beforeInit();
		init();
	}
	
	
	private void initContext(AppContext appContext) {
		this.appContext = appContext;
		runtimeStorage = appContext.runtimeStorage;
	}
	
	private void initActionFacades() {
		actionFacades = new ActionFacades(this);
		actionFacades.init();
		actionService = actionFacades;
	}

	
	

	protected void beforeInit(){ /* override if need */ };
	
	protected abstract void init();
	
	protected void afterAllInited(){ /* override if need */ };
	
	
	void setControllersPool(ControllersPool pool){
		controllersPool = pool;
	}

	public List<AbstractActionFacade> getActionFacades(){
		return actionFacades.getAll();
	}
	
	protected List<AbstractActionFacade> getSubActionFacades(){
		ArrayList<AbstractActionFacade> out = new ArrayList<AbstractActionFacade>();
		
		LinkedList<Container> queue = new LinkedList<Container>();
		if(ui instanceof Container){
			queue.addLast((Container)ui);
		}
		while(queue.size() > 0){
			Container container = queue.removeFirst();
			Component[] children = container.getComponents();
			for (Component child : children) {
				if(child instanceof Module<?>){
					out.addAll(((Module<?>) child).getMethodActions());
				}
				if(child instanceof Container){
					queue.addLast((Container)child);
				}
			}
		}
		
		Collections.sort(out, new ActionFacadeComparator());
		return out;
	}
	
	
	
	protected <N> N invoke(AbstractCommand<N> command) throws Exception {
		return (N) appContext.commandService.invoke(command);
	}
	
	protected <N> Answer<N> invokeSafe(AbstractCommand<N> command){
		return (Answer<N>) appContext.commandService.invokeSafe(command);
	}
	
	@SuppressWarnings({ "unchecked" })
	public <N extends AbstractController<T>> N getController(Class<N> clazz){
		if(controllersPool == null) throw new IllegalStateException("controllersPool is null");
		return (N) controllersPool.getController(clazz);
	}
	
	
	@SuppressWarnings("unchecked")
	protected <N> N instanceByMapping(String mapping){
		return (N) appContext.globalObjects.instanceByMapping(mapping);
	}
	
	protected void fireEvent(Event event){
		appContext.eventManager.fireEventInEDT(this, event);
	}
	
	protected <N> Answer<N> invokeSafe(Request<N> request){
		return (Answer<N>)appContext.eventManager.fireSaveRequestInEDT(this, request);
	}
	
	@Override
	public Object getDomainKey() {
		if(ui instanceof Component){
			return DomainUtil.findDomainKey((Component)ui);
		}
		return DomainMember.ROOT_DOMAIN_KEY;
	}
	
	public void invokeLater(Runnable runnable){
		SwingUtilities.invokeLater(runnable);
	}

}
