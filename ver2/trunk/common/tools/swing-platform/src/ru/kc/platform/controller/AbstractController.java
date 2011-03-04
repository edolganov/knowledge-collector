package ru.kc.platform.controller;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.platform.action.facade.AbstractActionFacade;
import ru.kc.platform.action.facade.ButtonFacadeMediator;
import ru.kc.platform.annotations.ExportAction;
import ru.kc.platform.app.AppContext;
import ru.kc.platform.command.AbstractCommand;
import ru.kc.platform.data.Answer;
import ru.kc.platform.domain.DomainMember;
import ru.kc.platform.domain.DomainUtil;
import ru.kc.platform.event.Event;
import ru.kc.platform.module.Module;
import ru.kc.platform.runtimestorage.RuntimeStorage;
import ru.kc.util.swing.icon.IconUtil;

public abstract class AbstractController<T> implements DomainMember {
	
	protected Log log = LogFactory.getLog(getClass());
	protected T ui;
	protected AppContext appContext;
	protected RuntimeStorage runtimeStorage;
	
	private List<AbstractActionFacade> actionFacades = new ArrayList<AbstractActionFacade>();
	private ControllersPool controllersPool;
	
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
		Class<?> curClass = getClass();
		while(!curClass.equals(AbstractController.class)){
			Method[] methods = curClass.getDeclaredMethods();
			for(Method candidat : methods){
				ExportAction annotation = candidat.getAnnotation(ExportAction.class);
				if(annotation != null){
					actionFacades.add(createButtonFacadeMediator(annotation, candidat));
				}
			}
			curClass = curClass.getSuperclass();
		}
	}
	
	private AbstractActionFacade createButtonFacadeMediator(ExportAction annotation, final Method method) {
		ButtonFacadeMediator mediator = new ButtonFacadeMediator();
		mediator.setIcon(IconUtil.get(annotation.icon()));
		mediator.setToolTipText(annotation.description());
		mediator.addListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					method.invoke(AbstractController.this);
				}catch (Exception ex) {
					log.error("invoke error for "+method,ex);
				}
				
			}
		});
		return mediator;
	}
	

	protected void beforeInit(){ /* override if need */ };
	
	protected abstract void init();
	
	protected void afterAllInited(){ /* override if need */ };
	
	
	void setControllersPool(ControllersPool pool){
		controllersPool = pool;
	}

	public List<AbstractActionFacade> getActionFacades(){
		return actionFacades;
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
	
	protected void fireEventInEDT(Event event){
		appContext.eventManager.fireEventInEDT(this, event);
	}
	
	@Override
	public Object getDomainKey() {
		if(ui instanceof Component){
			return DomainUtil.findDomainKey((Component)ui);
		}
		return DomainMember.ROOT_DOMAIN_KEY;
	}

}
