package ru.kc.common.node.edit;

import ru.kc.common.node.NodeConstants;
import ru.kc.common.node.edit.event.DescriptionChanged;
import ru.kc.common.node.edit.event.NameChanged;
import ru.kc.common.node.event.NodeUpdated;
import ru.kc.model.Node;
import ru.kc.platform.app.AppContext;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.platform.runtimestorage.RuntimeStorage;

public class NodeEditionsAggregator {
	
	private final String storageKey = "NodeEditionsAggregator-"+hashCode();
	
	RuntimeStorage runtimeStorage;
	NodeConstants nodeConstants;
	
	public void init(AppContext appContext, NodeConstants nodeConstants){
		appContext.eventManager.addObjectMethodListeners(this);
		runtimeStorage = appContext.runtimeStorage;
		this.nodeConstants = nodeConstants;
	}
	
	
	@EventListener(NameChanged.class)
	public void onEditing(NameChanged event){
		Node node = event.node;
		String edition = event.newName;
		getOrCreate(node).addEdition(nodeConstants.NAME, edition);
		//System.out.println(getOrCreate(node));
	}
	
	@EventListener(DescriptionChanged.class)
	public void onEditing(DescriptionChanged event){
		Node node = event.node;
		String edition = event.newDescription;
		getOrCreate(node).addEdition(nodeConstants.DESCRIPTION, edition);
		//System.out.println(getOrCreate(node));
	}
	
	@EventListener(NodeUpdated.class)
	public void onUpdated(NodeUpdated event){
		clearEditions(event.old);
	}


	public boolean hasEditions(Node node) {
		NodeEditions editions = get(node);
		return editions == null? false : editions.count() > 0;
	}
	
	public NodeEditions getEditions(Node node){
		NodeEditions out = get(node);
		if(out == null) out = new NodeEditions();
		return out;
	}
	
	private NodeEditions get(Node node){
		return runtimeStorage.get(node, storageKey);
	}
	
	private NodeEditions getOrCreate(Node node){
		NodeEditions out = runtimeStorage.get(node, storageKey);
		if(out == null){
			out = new NodeEditions();
			runtimeStorage.putWithWeakReferenceDomain(node, storageKey, out);
		}
		return out;
	}
	
	private void clearEditions(Node node){
		runtimeStorage.remove(node, storageKey);
	}

}
