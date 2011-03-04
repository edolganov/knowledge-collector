package ru.kc.platform.module;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.platform.action.facade.AbstractActionFacade;
import ru.kc.platform.app.AppContext;
import ru.kc.platform.controller.AbstractController;
import ru.kc.platform.controller.ControllerScan;
import ru.kc.platform.controller.ControllersPool;
import ru.kc.platform.service.ServiceContainer;
import ru.kc.platform.service.ServiceContainerImpl;
import ru.kc.platform.utils.AppUtils;

public abstract class ModuleController<T> {
	
	protected Object owner;
	protected T ui;
	private boolean inited = false;
	protected AppContext appContext;
	
	protected Log log;
	protected ControllersPool controllers;
	protected ServiceContainerImpl serviceContainer = new ServiceContainerImpl();

	
	
	
	public ModuleController(Object owner, T ui) {
		this.owner = owner;
		this.ui = ui;
		if(ui == null) throw new IllegalStateException("ui component is null");
		log = LogFactory.getLog(owner.getClass());
	}
	
	protected abstract void afterInit();
	
	protected abstract Class<?>[] getBlackList();
	
	public synchronized void setAppContext(AppContext context, String packagePreffix) {
		if(inited) return;
		
    	try {
			this.appContext = context;
			
			if(ui instanceof Component){
				appContext.componentScanner.scanAndInit((Component)ui);
			}
			
			ControllerScan controllerScan = new ControllerScan(this.appContext);
			controllers = controllerScan.scanAndInit(packagePreffix, ui, getBlackList());
			
			afterInit();
			inited = true;
			
			log.info("inited "+this);
    	}catch (Exception e) {
			log.error("error while inited "+this, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <N extends AbstractController<T>> N getController(Class<N> clazz){
		return (N)controllers.getController(clazz);
	}
	
	public List<AbstractActionFacade> getActionFacades() {
		ArrayList<AbstractActionFacade> out = new ArrayList<AbstractActionFacade>();
		for (AbstractController<?> c : controllers) {
			out.addAll(c.getActionFacades());
		}
		return out;
	}
	
	public AppContext getAppContext() {
		return appContext;
	}
	
	public boolean isInited() {
		return inited;
	}
	
	public T getUI() {
		return ui;
	}
	
	public ServiceContainer getServiceContainer(){
		return serviceContainer;
	}


	@Override
	public String toString() {
		return owner.getClass().getSimpleName() + " [inited=" + inited + ", ui=" + AppUtils.toStringLikeObject(ui) + ", controllers="
				+ controllers + "]";
	}










	

}
