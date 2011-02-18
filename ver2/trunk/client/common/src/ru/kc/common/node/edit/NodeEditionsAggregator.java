package ru.kc.common.node.edit;

import java.util.Collection;

import ru.kc.common.node.command.UpdateNode;
import ru.kc.common.node.edit.event.DescriptionChanged;
import ru.kc.common.node.edit.event.NameChanged;
import ru.kc.common.node.edit.event.NodeReverted;
import ru.kc.common.node.edit.event.RevertNodeRequest;
import ru.kc.common.node.edit.event.UpdateNodeRequest;
import ru.kc.common.node.event.NodeUpdated;
import ru.kc.model.Node;
import ru.kc.platform.app.AppContext;
import ru.kc.platform.command.CommandService;
import ru.kc.platform.event.EventManager;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.platform.runtimestorage.RuntimeStorage;
import ru.kc.tools.filepersist.update.UpdateDescription;
import ru.kc.tools.filepersist.update.UpdateName;
import ru.kc.tools.filepersist.update.UpdateRequest;
import ru.kc.util.Check;

public class NodeEditionsAggregator {
	
	private final String storageKey = "NodeEditionsAggregator-"+hashCode();
	
	RuntimeStorage runtimeStorage;
	CommandService commandService;
	EventManager eventManager;
	
	public void init(AppContext appContext){
		appContext.eventManager.addObjectMethodListeners(this);
		runtimeStorage = appContext.runtimeStorage;
		commandService = appContext.commandService;
		eventManager = appContext.eventManager;
	}
	
	
	@EventListener(NameChanged.class)
	public void onEditing(NameChanged event){
		Node node = event.node;
		String edition = event.newName;
		if(!Check.isEmpty(edition)){
			getOrCreate(node).add(new UpdateName(edition));
		}
		//System.out.println(getOrCreate(node));
	}
	
	@EventListener(DescriptionChanged.class)
	public void onEditing(DescriptionChanged event){
		Node node = event.node;
		String edition = event.newDescription;
		getOrCreate(node).add(new UpdateDescription(edition));
		//System.out.println(getOrCreate(node));
	}
	
	@EventListener(UpdateNodeRequest.class)
	public void onUpdateRequest(UpdateNodeRequest event){
		Node node = event.node;
		NodeEditions editions = get(node);
		if(editions != null){
			Collection<UpdateRequest> updates = editions.all();
			if(Check.isEmpty(updates)) return;
			commandService.invokeSafe(new UpdateNode(node, updates));
		}
	}
	
	@EventListener(RevertNodeRequest.class)
	public void onRevertRequest(RevertNodeRequest event){
		Node node = event.node;
		NodeEditions editions = get(node);
		if(editions != null){
			editions.removeAll();
			eventManager.fireEventInEDT(this, new NodeReverted(node));
		}
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
