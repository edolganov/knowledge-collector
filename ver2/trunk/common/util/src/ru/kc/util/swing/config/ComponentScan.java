package ru.kc.util.swing.config;

import java.awt.Component;
import java.awt.Container;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class ComponentScan {
	
	// p2ch = parent to child
	private ArrayList<ObjectHandler> p2chListeners = new ArrayList<ObjectHandler>();
	private ArrayList<ObjectHandler> ch2pListeners = new ArrayList<ObjectHandler>();
	
	//already inited objects
	private HashMap<Integer, WeakReference<Object>> initedObjects = new HashMap<Integer, WeakReference<Object>>();
	
	public void addListener(ObjectHandler l){
		ProcessPhase phase = l.getPhase();
		if(phase == ProcessPhase.FROM_CHILD_TO_PARENT){
			ch2pListeners.add(l);
		}else if(phase == ProcessPhase.FROM_PARENT_TO_CHILD){
			p2chListeners.add(l);
		}else throw new IllegalArgumentException("ConfigListener phase is null for "+l);
	}
	
	public void scanAndInit(Component root){
		LinkedList<Component> queue = new LinkedList<Component>();
		queue.addLast(root);
        LinkedList<Component> revertComponentStack = new LinkedList<Component>();
		
		while(!queue.isEmpty()){
			//process candidat
			Component candidat = queue.removeFirst();
			
			if(alreadyInited(candidat)) continue;
			for (ObjectHandler l : p2chListeners) l.process(candidat);
			
			revertComponentStack.addFirst(candidat);
			
			//add children
			if(candidat instanceof Container){
				Component[] children = ((Container)candidat).getComponents();
				if(children != null){
					for (Component component : children) {
						queue.addLast(component);
					}
				}
			}
		}
		
		while(!revertComponentStack.isEmpty()){
			Component component = revertComponentStack.removeFirst();
			for (ObjectHandler l : ch2pListeners) l.process(component);
		}
		
		addToInited(root);
	}

	private void addToInited(Component root) {
		initedObjects.put(root.hashCode(), new WeakReference<Object>(root));
	}

	private boolean alreadyInited(Component candidat) {
		int key = candidat.hashCode();
		WeakReference<Object> ref = initedObjects.get(key);
		if(ref == null) return false;
		
		Object ob = ref.get();
		if(ob == null){
			initedObjects.remove(key);
			return false;
		}
		
		return true;
	}

}
