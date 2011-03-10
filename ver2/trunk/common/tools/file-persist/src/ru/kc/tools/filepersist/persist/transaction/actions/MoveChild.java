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
		
		//TODO check logic
		
		oldParent.removeChild(child);
		newParent.addChild(child);
		child.setParent(newParent);
		
		return null;
	}

	@Override
	public void rollback() throws Exception {
		newParent.removeChild(child);
		oldParent.addChild(child);
		child.setParent(oldParent);
	}

}
