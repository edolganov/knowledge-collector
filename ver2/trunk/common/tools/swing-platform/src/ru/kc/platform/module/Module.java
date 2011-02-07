package ru.kc.platform.module;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.platform.actions.MethodAction;
import ru.kc.platform.app.AppContext;
import ru.kc.platform.controller.AbstractController;
import ru.kc.platform.controller.ControllerScan;
import ru.kc.platform.utils.AppUtils;

public abstract class Module<T extends Component> extends JPanel {

	private static final long serialVersionUID = 3201710095656034030L;
	
	protected T ui;
	protected AppContext appContext;
	protected Log log = LogFactory.getLog(getClass());
	protected List<AbstractController<T>> controllers = new ArrayList<AbstractController<T>>(0);
	
	private boolean inited = false;
	
	
	
	public Module() {
		super();
		this.ui = createUI();
		if(ui == null) throw new IllegalStateException("ui component is null");
		
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
		if(inited) return;
		else {
			AppContext context = null;
			Container parent = getParent();
			
			Object key = null;
			while(parent != null) {
				if(parent instanceof Module<?>){
					context = ((Module<?>)parent).appContext;
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
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public synchronized void setAppContext(AppContext context) {
		if(inited) return;
		
    	try {
			this.appContext = context;
			
			ControllerScan controllerScan = new ControllerScan(this.appContext);
			controllers = (List)controllerScan.scanAndInit(this.getClass().getPackage().getName(), ui, getBlackList());
			
			afterInit();
			inited = true;
			
			log.info("inited "+this);
    	}catch (Exception e) {
			log.error("error while inited "+this, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <N extends AbstractController<T>> N getController(Class<N> clazz){
		for (AbstractController<T> candidat : controllers) {
			if(candidat.getClass().equals(clazz)) return (N)candidat;
		}
		return null;
	}
	
	public List<MethodAction> getMethodActions(){
		ArrayList<MethodAction> out = new ArrayList<MethodAction>();
		for (AbstractController<T> c : controllers) {
			out.addAll(c.getMethodActions());
		}
		return out;
	}


	@Override
	public String toString() {
		return getClass().getSimpleName() + " [inited=" + inited + ", ui=" + AppUtils.toStringLikeObject(ui) + ", controllers="
				+ controllers + "]";
	}


	

}
