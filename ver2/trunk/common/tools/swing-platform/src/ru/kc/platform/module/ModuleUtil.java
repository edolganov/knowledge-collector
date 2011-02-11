package ru.kc.platform.module;

import java.awt.Component;
import java.awt.Container;
import java.util.LinkedList;

import ru.kc.platform.controller.AbstractController;
import ru.kc.platform.controller.ControllersPool;
import ru.kc.platform.event.EventManager;

public class ModuleUtil {
	
	public static void removeAllLisreners(DialogModule<?> module){
		EventManager eventManager = module.controller.appContext.eventManager;
		eventManager.removeObjectMethodListener(module);
		
		removeAllListeners(eventManager,module.controller.controllers);
		removeAllLisreners(module.controller.ui);
	}

	public static void removeAllLisreners(Component root){
		LinkedList<Component> modulesQueue = new LinkedList<Component>();
		modulesQueue.addLast(root);
		
		while(!modulesQueue.isEmpty()){
			Component candidat = modulesQueue.removeFirst();
			if(candidat instanceof Module){
				Module<?> module = (Module<?>)candidat;
				EventManager eventManager = module.controller.appContext.eventManager;
				eventManager.removeObjectMethodListener(module);
				removeAllListeners(eventManager,module.controller.controllers);
			}

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
	
	private static void removeAllListeners(EventManager eventManager, ControllersPool controllers) {
		if(controllers != null){
			for(AbstractController<?> c : controllers){
				eventManager.removeObjectMethodListener(c);
			}
		}
		
	}
	
	

}
