package ru.kc.platform.module;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.List;

import javax.swing.JPanel;

import ru.kc.platform.action.MethodAction;
import ru.kc.platform.app.AppContext;
import ru.kc.platform.controller.AbstractController;
import ru.kc.platform.domain.DomainMember;
import ru.kc.platform.domain.DomainUtil;
import ru.kc.platform.domain.annotation.Domain;
import ru.kc.platform.service.ServiceContainer;

public abstract class Module<T extends Component> extends JPanel implements DomainMember, ServiceContainer {

	private static final long serialVersionUID = 3201710095656034030L;
	
	protected ModuleController<T> controller;
	
	public Module() {
		super();
		T ui = createUI();
		controller = new ModuleController<T>(this, ui) {

			@Override
			protected void afterInit() {
				Module.this.afterInit();
			}

			@Override
			protected Class<?>[] getBlackList() {
				return Module.this.getBlackList();
			}
		};
		
		setLayout(new BorderLayout());
		add(ui,BorderLayout.CENTER);
		
		addHierarchyListener(new HierarchyListener() {
			
			@Override
			public void hierarchyChanged(HierarchyEvent e) {
				if ((e.getChangeFlags() & HierarchyEvent.PARENT_CHANGED) != 0) {
					tryInit();
				}
			}
		});
	}
	
	
	protected abstract T createUI();
	
	protected void afterInit(){ /*override if need*/}
	
	protected Class<?>[] getBlackList(){
		/*override if need*/
		return null;
	}
	
	public void tryInit() {
		if(controller.isInited()) return;

		AppContext context = null;
		Container parent = getParent();
		
		Object key = null;
		while(parent != null) {
			if(parent instanceof Module<?>){
				context = ((Module<?>)parent).controller.getAppContext();
				if(context != null) break;
			}
			key = parent;
			parent = parent.getParent();
		}
		if(context == null && key != null){
			context = AppContext.get(key);
		}
		
		if(context == null) return;
		setAppContext(context);
		
	}
	
	
	public <N extends AbstractController<T>> N getController(Class<N> clazz){
		return (N)controller.getController(clazz);
	}
	
	public List<MethodAction> getMethodActions(){
		return controller.getMethodActions();
	}
	
	public void setAppContext(AppContext context) {
		controller.setAppContext(context, getClass().getPackage().getName());
	}
	
	public void removeAllListneres() {
		removeAllListneres(this);
	}
	
	public static void removeAllListneres(Component component){
		ModuleUtil.removeAllLisreners(component);
	}
	
	@Override
	public Object getDomainKey() {
		Domain domainFlag = getClass().getAnnotation(Domain.class);
		if(domainFlag != null){
			return this;
		}
		
		Container parent = getParent();
		if(parent != null){
			return DomainUtil.findDomainKey(parent);
		}
		return DomainMember.ROOT_DOMAIN_KEY;
	}
	

	@Override
	public void addService(Class<?> type, Object ob) {
		controller.getServiceContainer().addService(type, ob);
	}
	
	@Override
	public boolean containsService(Class<?> type) {
		return controller.getServiceContainer().containsService(type);
	}
	
	@Override
	public Object getService(Class<?> type) {
		return controller.getServiceContainer().getService(type);
	}
	

	@Override
	public String toString() {
		return controller.toString();
	}





	

}
