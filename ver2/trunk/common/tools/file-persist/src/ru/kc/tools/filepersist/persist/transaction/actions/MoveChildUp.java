package ru.kc.tools.filepersist.persist.transaction.actions;

import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.transaction.AtomicAction;

public class MoveChildUp extends AtomicAction<Integer>{
	
	NodeBean node;
	NodeBean parent;

	


	public MoveChildUp(NodeBean parent, NodeBean node) {
		super();
		this.parent = parent;
		this.node = node;
	}

	@Override
	public Integer invoke() throws Throwable {
		return parent.moveChildUp(node);
	}
	
	@Override
	public void rollback() throws Exception {
		parent.moveChildDown(node);
	}

}
