package ru.kc.main.command.basic;

import ru.kc.main.common.Dialogs;
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
			boolean confirm = Dialogs.confirmByDialog(rootUI, "Подтверждение удаления", "Удалить "+node.getName()+"?");
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
