package ru.kc.common.controller;

import java.awt.Frame;
import java.util.Collection;
import java.util.List;

import ru.kc.common.Context;
import ru.kc.common.dialog.Dialogs;
import ru.kc.common.node.event.ChildAdded;
import ru.kc.common.node.event.ChildDeletedRecursive;
import ru.kc.common.node.event.NodeMoved;
import ru.kc.common.node.event.NodeUpdated;
import ru.kc.model.Node;
import ru.kc.platform.annotations.Inject;
import ru.kc.platform.controller.AbstractController;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.tools.filepersist.Factory;
import ru.kc.tools.filepersist.PersistService;
import ru.kc.tools.filepersist.Tree;
import ru.kc.tools.filepersist.Updater;
import ru.kc.tools.filepersist.update.UpdateRequest;

public abstract class Controller<T> extends AbstractController<T>{
	
	@Inject
	protected Context context;
	protected PersistService persist;
	protected Tree persistTree;
	protected Factory persistFactory;
	protected Frame rootUI;
	protected Updater updater;
	protected Dialogs dialogs;
	
	@Override
	protected void beforeInit() {
		persist = context.persistService;
		persistTree = persist.tree();
		persistFactory = persist.factory();
		rootUI = (Frame)appContext.rootUI;
		updater = persist.updater();
		dialogs = context.dialogs;
	}
	
	protected void onChildAdded(Node parent, Node child){ /* override if need */ }
	
	protected void onChildDeletedRecursive(Node parent, Node deletedChild, List<Node> deletedSubChildren){ /* override if need */ }
	
	protected void onNodeUpdated(Node old, Node updatedNode, Collection<UpdateRequest> updates){ /* override if need */ }
	
	protected void onNodeMoved(Node oldParent, Node node, Node newParent){ /* override if need */ }
	
	
	@EventListener
	public void _onChildAdded(ChildAdded event){
		onChildAdded(event.parent, event.child);
	}
	
	@EventListener
	public void _onChildDeletedRecursive(ChildDeletedRecursive event){
		onChildDeletedRecursive(event.parent, event.deletedChild, event.deletedSubChildren);
	}
	
	@EventListener
	public void _onNodeUpdated(NodeUpdated event){
		onNodeUpdated(event.old, event.node, event.updates);
	}
	
	@EventListener
	public void _onNodeMoved(NodeMoved event){
		onNodeMoved(event.oldParent, event.node, event.newParent);
	}
	

}
