package ru.kc.tools.filepersist.persist.transaction.actions;

import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.transaction.AtomicAction;

public class CanMoveChild extends AtomicAction<Boolean>{
	
	NodeBean child;
	NodeBean newParent;

	public CanMoveChild(NodeBean child, NodeBean newParent) {
		super();
		this.child = child;
		this.newParent = newParent;
	}

	@Override
	public Boolean invoke() throws Throwable {
		//проверяем что предок не перемещается в потомка
		NodeBean[] targetPath = t.invoke(new GetNodePath(newParent));
		NodeBean[] candidatPath = t.invoke(new GetNodePath(child));
		boolean valid = false;
		if(candidatPath.length > targetPath.length) 
			valid = true;
		else {
			for (int i = 0; i < candidatPath.length; i++) {
				if(!candidatPath[i].equals(targetPath[i])){
					valid = true;
					break;
				}
			}
		}
		return valid;
	}

	@Override
	public void rollback() throws Exception {}

}
