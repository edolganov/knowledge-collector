package ru.kc.tools.filepersist.persist.transaction.actions;

import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.transaction.AtomicAction;

public class MoveChild extends AtomicAction<Void>{
	
	NodeBean oldParent;
	NodeBean child;
	NodeBean newParent;

	public MoveChild(NodeBean child, NodeBean newParent) {
		super();
		this.child = child;
		this.newParent = newParent;
	}

	@Override
	public Void invoke() throws Throwable {
		oldParent = t.invoke(new GetParent(child));
		if(oldParent == null)
			throw new IllegalStateException("parent null for "+child);
		
		checkChildToMove(child, newParent);
		
		oldParent.removeChild(child);
		newParent.addChild(child);
		child.setParent(newParent);
		
		return null;
	}

	private void checkChildToMove(NodeBean child, NodeBean newParent) throws Throwable {
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
		if(!valid) 
			throw new IllegalStateException("invalid move destination "+newParent+" for "+child);
	}

	@Override
	public void rollback() throws Exception {
		newParent.removeChild(child);
		oldParent.addChild(child);
		child.setParent(oldParent);
	}

}
