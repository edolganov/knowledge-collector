package ru.kc.common.node.edit;

import java.util.HashMap;
import java.util.Map;

public class NodeEditions {
	
	private HashMap<String, Object> editionsByType = new HashMap<String, Object>();
	
	public void addEdition(String type, Object value){
		editionsByType.put(type, value);
	}
	
	Map<String, Object> editionsByType(){
		return editionsByType;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String type){
		return (T)editionsByType.get(type);
	}
	
	public void remove(String type){
		editionsByType.remove(type);
	}
	
	public int count(){
		return editionsByType.size();
	}

	@Override
	public String toString() {
		return "NodeEditions [byType=" + editionsByType + "]";
	}
	
	

}
