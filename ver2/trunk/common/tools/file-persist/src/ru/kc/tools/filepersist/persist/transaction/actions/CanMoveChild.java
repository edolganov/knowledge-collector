package ru.kc.tools.filepersist.persist.transaction.actions;

import java.util.List;

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
		List<NodeBean> targetPath = t.invoke(new GetNodePath(newParent));
		List<NodeBean> candidatPath = t.invoke(new GetNodePath(child));
		boolean valid = false;
		if(candidatPath.size() > targetPath.size()) 
			valid = true;
		else {
			for (int i = 0; i < candidatPath.size(); i++) {
				if(!candidatPath.get(i).equals(targetPath.get(i))){
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
