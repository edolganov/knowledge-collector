package ru.kc.module.search.tools;

import java.util.Comparator;
import java.util.LinkedHashMap;

import ru.kc.model.Dir;
import ru.kc.model.FileLink;
import ru.kc.model.Link;
import ru.kc.model.Node;
import ru.kc.model.Text;
import ru.kc.util.collection.Pair;

public class GroupComparator {
	
	private static LinkedHashMap<Class<?>, Integer> typeIndexes = new LinkedHashMap<Class<?>, Integer>();
	static {
		typeIndexes.put(Dir.class, 0);
		typeIndexes.put(Link.class, 1);
		typeIndexes.put(Text.class, 2);
		typeIndexes.put(FileLink.class, 3);
		typeIndexes.put(Node.class, Integer.MAX_VALUE); //unknow
	}
	
	public int getGroupsCount(){
		return typeIndexes.size();
	}
	
	public Pair<Integer, Class<?>> getIndex(Class<?> type){
		Integer index = null;
		Class<?> foundType = null;
		for(Class<?> candidat : typeIndexes.keySet()){
			if(candidat.isAssignableFrom(type)){
				index = typeIndexes.get(candidat);
				foundType = candidat;
				break;
			}
		}
		
		if(index == null) 
			throw new IllegalStateException("index is null");
		return new Pair<Integer, Class<?>>(index, foundType);
	}

}
