package ru.kc.platform.event.model;

import java.util.ArrayList;
import java.util.Iterator;

public class ListenersQueue implements Iterable<MethodListener>{
	
	ArrayList<MethodListener> list = new ArrayList<MethodListener>();
	
	public void add(MethodListener listener) {
		if(listener.isFirst()) addFirst(listener);
		else if(listener.isLast()) addLast(listener);
		else addUnsorted(listener);
		
	}
	
	private void addFirst(MethodListener listener) {
		int insertIndex = 0;
		for(MethodListener l : list){
			if(!l.isFirst()) break;
			insertIndex++;
		}
		list.add(insertIndex,listener);
	}
	
	private void addLast(MethodListener listener) {
		int size = list.size();
		int insertIndex = size;
		for (int i = size-1; i > -1; i--) {
			if(!list.get(i).isLast()) break;
			insertIndex--;
		}
		list.add(insertIndex,listener);
	}

	private void addUnsorted(MethodListener listener) {
		addLast(listener);
	}

	public int removeLike(MethodListener listener) {
		int count = 0;
		for (int i = list.size()-1; i > -1; i--) {
			MethodListener candidat = list.get(i);
			if(candidat.like(listener)){
				list.remove(i);
				count++;
			}
		}
		return count;
	}
	
	public void removeInvalidListeners() {
		for (int i = list.size()-1; i > -1; i--) {
			MethodListener candidat = list.get(i);
			if(!candidat.isValid()){
				list.remove(i);
			}
		}
	}
	
	@Override
	public Iterator<MethodListener> iterator() {
		return list.iterator();
	}

	@Override
	public String toString() {
		return "ListenersQueue [list=" + list + "]";
	}


}
