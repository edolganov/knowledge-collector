package ru.kc.common.node.edit;

import java.util.Collection;
import java.util.HashMap;

import ru.kc.tools.filepersist.update.UpdateRequest;

public class NodeEditions {
	
	private HashMap<Class<?>, UpdateRequest> updatesByType = new HashMap<Class<?>, UpdateRequest>();
	
	public void add(UpdateRequest updateRequest){
		updatesByType.put(updateRequest.getClass(), updateRequest);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(Class<? extends UpdateRequest> type){
		return (T)updatesByType.get(type).value();
	}
	
	public void remove(Class<? extends UpdateRequest> type){
		updatesByType.remove(type);
	}
	
	public Collection<UpdateRequest> all(){
		return  updatesByType.values();
	}
	
	public int count(){
		return updatesByType.size();
	}
	

	@Override
	public String toString() {
		return "NodeEditions [byType=" + updatesByType + "]";
	}
	
	

}
