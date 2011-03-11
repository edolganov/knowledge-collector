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
		boolean valid = t.invoke(new CanMoveChild(child, newParent));
		if(!valid) 
			throw new IllegalArgumentException("invalid move destination "+newParent+" for "+child);
	}

	@Override
	public void rollback() throws Exception {
		newParent.removeChild(child);
		oldParent.addChild(child);
		child.setParent(oldParent);
	}

}
