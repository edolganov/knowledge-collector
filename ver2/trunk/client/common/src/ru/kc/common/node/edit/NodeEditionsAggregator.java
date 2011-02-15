package ru.kc.common.node.edit;

import ru.kc.common.node.edit.event.DescriptionChanged;
import ru.kc.common.node.edit.event.NameChanged;
import ru.kc.model.Node;
import ru.kc.platform.app.AppContext;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.platform.runtimestorage.RuntimeStorage;

public class NodeEditionsAggregator {
	
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	
	private static final String KEY = "NodeEditionsAggregator";
	
	RuntimeStorage runtimeStorage;
	
	public void init(AppContext appContext){
		appContext.eventManager.addObjectMethodListeners(this);
		runtimeStorage = appContext.runtimeStorage;
	}
	
	
	@EventListener(NameChanged.class)
	public void onEditing(NameChanged event){
		Node node = event.node;
		String edition = event.newName;
		getOrCreate(node).addEdition(NAME, edition);
		
		System.out.println(getOrCreate(node));
	}
	
	@EventListener(DescriptionChanged.class)
	public void onEditing(DescriptionChanged event){
		Node node = event.node;
		String edition = event.newDescription;
		getOrCreate(node).addEdition(DESCRIPTION, edition);
		
		System.out.println(getOrCreate(node));
	}
	
	
	
	private NodeEditions getOrCreate(Node node){
		NodeEditions out = runtimeStorage.get(node, KEY);
		if(out == null){
			out = new NodeEditions();
			runtimeStorage.putWithWeakReferenceDomain(node, KEY, out);
		}
		return out;
	}

}
