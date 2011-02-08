package ru.kc.platform.controller;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.platform.action.MethodAction;
import ru.kc.platform.app.AppContext;
import ru.kc.platform.command.AbstractCommand;
import ru.kc.platform.module.Module;

public abstract class AbstractController<T> {
	
	protected Log log = LogFactory.getLog(getClass());
	protected AppContext appContext;
	protected T ui;
	private List<MethodAction> methodActions;
	
	void setMethodActions(List<MethodAction> actions){
		methodActions = Collections.unmodifiableList(actions);
	}
	
	void init(AppContext appContext, T ui){
		setAppContext(appContext);
		this.ui = ui;
		beforeInit();
		init();
	}
	
	protected void beforeInit(){ /* override if need */ };
	
	protected abstract void init();


	public List<MethodAction> getMethodActions(){
		return methodActions;
	}

	private void setAppContext(AppContext appContext) {
		this.appContext = appContext;
	}
	
	protected List<MethodAction> getSubActionsRecursive(){
		ArrayList<MethodAction> out = new ArrayList<MethodAction>();
		
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
		
		return out;
	}
	
	
	
	protected <N> N invoke(AbstractCommand<N> command) throws Exception {
		return (N) appContext.commandService.invoke(command, appContext);
	}
	
//	protected <N> N invokeSafe(AbstractCommand<N> command){
//		try{
//			invoke(command);
//		}catch (Exception e) {
//			log.error(e);
//		}
//	}
	
	

}
