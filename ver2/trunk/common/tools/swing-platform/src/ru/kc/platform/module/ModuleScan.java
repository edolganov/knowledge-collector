package ru.kc.platform.module;

import java.awt.*;
import java.util.LinkedList;

import ru.kc.platform.app.AppContext;

public class ModuleScan {
	
	private AppContext appContext;

	public ModuleScan(AppContext appContext) {
		this.appContext = appContext;
	}

	@SuppressWarnings("rawtypes")
	public void scanAndInit(Container root) {
		LinkedList<Container> queue = new LinkedList<Container>();
		queue.addLast(root);
        LinkedList<Module> moduleStack=new LinkedList<Module>();
		
		while(!queue.isEmpty()){
			//process candidat
			Container candidat = queue.removeFirst();
			if(candidat instanceof Module){
                moduleStack.addFirst((Module)candidat);
			}
			
			//add children
			Component[] children = candidat.getComponents();
			if(children != null){
				for (Component component : children) {
					if(component instanceof Container){
						queue.addLast((Container)component);
					}
				}
			}
		}
        while(!moduleStack.isEmpty()){
            Module<?> module = moduleStack.removeFirst();
			module.setAppContext(appContext);
        }

	}

}
