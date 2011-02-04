package ru.kc.platform.module;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.platform.app.AppContext;
import ru.kc.platform.utils.AppUtils;

public abstract class Module<T extends Component> extends JPanel {

	private static final long serialVersionUID = 3201710095656034030L;
	
	protected T ui;
	protected AppContext appContext;
	protected Log log = LogFactory.getLog(getClass());
	
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
			Module<?> parentModule = null;
			Container parent = getParent();
			
			while(parent != null) {
				if(parent instanceof Module<?>){
					parentModule = (Module<?>)parent;
					break;
				} else{
					parent = parent.getParent();
				}
			}
			
			if(parentModule == null) return;
			AppContext context = parentModule.appContext;
			if(context == null){
				log.error("appContext is null from parent "+parentModule);
				return;
			}
			setAppContext(context);
		}
	}
	
	
	public synchronized void setAppContext(AppContext context) {
		if(inited) return;
		
    	try {
			this.appContext = context;
			
			//new ControllerScan(this.appContext).scanAndInit(this.getClass().getPackage().getName(), ui,getBlackList());
			//appContext.getRuntimeConfig().scanAndInit(ui);
			afterInit();
			inited = true;
    	}catch (Exception e) {
			log.error("error by set context", e);
		}
	}


	@Override
	public String toString() {
		return getClass().getSimpleName()+" [ui=" + AppUtils.toStringLikeObject(ui) + ", inited=" + inited + "]";
	}

}
