package ru.kc.util.collection;

import java.util.List;

public class ListUtil {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int moveUp(List list, Object child){
		int oldIndex = list.indexOf(child);
		if(oldIndex == -1) return -1;
		
		int newIndex = oldIndex - 1;
		if(newIndex == -1){
			newIndex = list.size()-1;
		}
		list.remove(oldIndex);
		list.add(newIndex, child);
		return newIndex;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int moveDown(List list, Object child){
		int oldIndex = list.indexOf(child);
		if(oldIndex == -1) return -1;
		
		int newIndex = oldIndex + 1;
		if(newIndex == list.size()){
			newIndex = 0;
		}
		list.remove(oldIndex);
		list.add(newIndex, child);
		return newIndex;
	}

}
