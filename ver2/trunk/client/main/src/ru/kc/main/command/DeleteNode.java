package ru.kc.main.command;

import ru.kc.main.common.RollbackableCommand;
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
			
		}
		System.out.println("delete!");
		return null;
	}

	@Override
	public void rollback() throws Exception {
		//TODO
	}

}
