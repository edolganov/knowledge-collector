package ru.kc.tools.filepersist.persist.transaction;

import java.util.ArrayList;
import java.util.HashMap;

public class ActionListeners {
	
	@SuppressWarnings("rawtypes")
	private HashMap<Class<?>, ArrayList<AtomicActionListener>> map = new HashMap<Class<?>, ArrayList<AtomicActionListener>>();

	@SuppressWarnings("rawtypes")
	public <T extends AtomicAction<?>> void addListener(Class<T> type, AtomicActionListener<T> listener){
		ArrayList<AtomicActionListener> list = map.get(type);
		if(list == null){
			list = new ArrayList<AtomicActionListener>();
			map.put(type, list);
		}
		list.add(listener);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void fireInvoke(AtomicAction<?> action) throws Throwable{
		Class<?> type = action.getClass();
		ArrayList<AtomicActionListener> list = map.get(type);
		if(list != null){
			for(AtomicActionListener l : list){
				l.onInvoke(action);
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void fireCommit(AtomicAction<?> action) throws Throwable{
		Class<?> type = action.getClass();
		ArrayList<AtomicActionListener> list = map.get(type);
		if(list != null){
			for(AtomicActionListener l : list){
				l.onCommit(action);
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void fireRollback(AtomicAction<?> action) throws Throwable{
		Class<?> type = action.getClass();
		ArrayList<AtomicActionListener> list = map.get(type);
		if(list != null){
			for(AtomicActionListener l : list){
				l.onRollback(action);
			}
		}
	}
	
}
