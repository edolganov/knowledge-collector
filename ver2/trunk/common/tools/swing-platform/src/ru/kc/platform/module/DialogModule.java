package ru.kc.platform.module;

import java.awt.Container;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

import ru.kc.platform.app.AppContext;
import ru.kc.platform.controller.AbstractController;
import ru.kc.util.swing.dialog.DialogUtil;

public abstract class DialogModule<T extends JDialog>{
	
	protected ModuleController<T> controller;
	
	public DialogModule() {
		super();
	}
	
	public T createDialog(Frame owner, boolean modal){
		T ui = createUI(owner,modal);
		controller = new ModuleController<T>(DialogModule.this, ui) {

			@Override
			protected void afterInit() {
				DialogModule.this.afterInit();
			}

			@Override
			protected Class<?>[] getBlackList() {
				return DialogModule.this.getBlackList();
			}
		};
		
		DialogUtil.init(ui);
		tryInit();
		
		ui.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosed(WindowEvent e) {
				removeAllListneres();
			}
			
		});
		
		return ui;
	}
	
	protected abstract T createUI(Frame parent, boolean modal);
	
	protected void afterInit(){ /*override if need*/}
	
	protected Class<?>[] getBlackList(){
		/*override if need*/
		return null;
	}
	
	public T getDialogUI(){
		return controller.getUI();
	}
	
	private void removeAllListneres() {
		ModuleUtil.removeAllLisreners(this);
	}

	public void createAndShow(Frame owner, boolean modal){
		createDialog(owner, modal).setVisible(true);
	}
	
	public void show(){
		if(controller.ui == null) throw new IllegalStateException("UI is null. Call createDialog() before.");
		controller.ui.setVisible(true);
	}
	
	
	private void tryInit() {
		if(controller.isInited()) return;
		
		AppContext context = null;
		Container parent = controller.getUI().getParent();
		
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
		controller.setAppContext(context, getClass().getPackage().getName());
	}
	
	public <N extends AbstractController<T>> N getController(Class<N> clazz){
		return (N)controller.getController(clazz);
	}

}
