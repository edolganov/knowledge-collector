package ru.kc.common.node.command;

import ru.kc.common.command.RollbackableCommand;
import ru.kc.model.Node;

public class DeleteNode extends RollbackableCommand<Void> {
	
	Node node;
	boolean showConfirmDialog;

	public DeleteNode(Node node) {
		this(node, true);
	}
	
	public DeleteNode(Node node,boolean showConfirmDialog) {
		super();
		this.node = node;
		this.showConfirmDialog = showConfirmDialog;
	}

	@Override
	protected Void invoke() throws Exception {
		if(node == null) return null;
		
		if(showConfirmDialog){
			boolean confirm = dialogs.confirmByDialog(rootUI, "Confirm the delete operation", "Delete "+node.getName()+"?");
			if(!confirm) return null;
		}
		
		persistTree.deleteRecursive(node);
		return null;
	}

	@Override
	public void rollback() throws Exception {
		//TODO
	}

}
