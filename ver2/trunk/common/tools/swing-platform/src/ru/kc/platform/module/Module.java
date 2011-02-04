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

import ru.kc.platform.app.AppContext;
import ru.kc.platform.controller.Controller;
import ru.kc.platform.controller.ControllerScan;
import ru.kc.platform.utils.AppUtils;

public abstract class Module<T extends Component> extends JPanel {

	private static final long serialVersionUID = 3201710095656034030L;
	
	protected T ui;
	protected AppContext appContext;
	protected Log log = LogFactory.getLog(getClass());
	protected List<Controller<T>> controllers = new ArrayList<Controller<T>>(0);
	
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
			
			while(parent != null) {
				if(parent instanceof Module<?>){
					context = ((Module<?>)parent).appContext;
					if(context != null) break;
				}
				parent = parent.getParent();
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
    	}catch (Exception e) {
			log.error("error by set context", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <N extends Controller<T>> N getController(Class<N> clazz){
		for (Controller<T> candidat : controllers) {
			if(candidat.getClass().equals(clazz)) return (N)candidat;
		}
		return null;
	}


	@Override
	public String toString() {
		return getClass().getSimpleName()+" [ui=" + AppUtils.toStringLikeObject(ui) + ", inited=" + inited + "]";
	}

}
