package ru.kc.platform.module;

import java.awt.Component;
import java.awt.Container;
import java.util.LinkedList;

import ru.kc.platform.event.EventManager;

public class ModuleUtil {
	
	public static void removeAllLisreners(DialogModule<?> module){
		EventManager eventManager = module.controller.appContext.eventManager;
		//MsgManager msgManager = module.appContext.getMsgManager();
		
		eventManager.removeObjectMethodListener(module);
		//msgManager.removeObjectMethodListener(module);
		
		//TODO было неожиданное удаление контроллера, когда найдем причину вернуть обратно
		//Controller.removeAll(module.ui);
		
		//process child
		removeAllLisreners(module.controller.ui);
	}
	
	public static void removeAllLisreners(Component root){
		LinkedList<Component> modulesQueue = new LinkedList<Component>();
		modulesQueue.addLast(root);
		
		while(!modulesQueue.isEmpty()){
			Component candidat = modulesQueue.removeFirst();
			if(candidat instanceof Module){
				//process module
				Module<?> module = (Module<?>)candidat;
				//System.out.println("ModuleUtil: clear " + module);
				EventManager eventManager = module.controller.appContext.eventManager;
				//MsgManager msgManager = module.controller.appContext.getMsgManager();
				
				eventManager.removeObjectMethodListener(module);
				//msgManager.removeObjectMethodListener(module);
				
				//Component ui = module.controller.ui;
				//TODO было неожиданное удаление контроллера, когда найдем причину вернуть обратно
				//Controller.removeAll(module.ui);
			}

			//add children
			if(candidat instanceof Container){
				Component[] children = ((Container)candidat).getComponents();
				if(children != null){
					for (Component component : children) {
						modulesQueue.addLast(component);
					}
				}
			}
		}
	}
	
	

}
