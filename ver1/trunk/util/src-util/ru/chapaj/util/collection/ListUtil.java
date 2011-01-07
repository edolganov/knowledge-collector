package ru.chapaj.util.collection;

import java.util.List;

public class ListUtil {
	
	public static <T> void move(List<T> list, T element, int position){
		int i = list.indexOf(element);
		if(i > -1){
			list.remove(i);
			list.add(position, element);
		}
	}
	
	
	public static <T> void moveDown(List<T> list, T element){
		int i = list.indexOf(element);
		if(i < list.size()-1) move(list, element, i+1);
	}
	
	public static <T> void moveUp(List<T> list, T element){
		int i = list.indexOf(element);
		if(i > 0) move(list, element, i-1);
	}

}
