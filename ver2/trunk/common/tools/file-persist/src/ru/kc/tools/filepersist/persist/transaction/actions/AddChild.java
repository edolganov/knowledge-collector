package ru.kc.tools.filepersist.persist.transaction.actions;

import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.transaction.AtomicAction;

public class AddChild extends AtomicAction<Void>{
	
	NodeBean parent;
	NodeBean node;

	public AddChild(NodeBean parent, NodeBean node) {
		super();
		this.parent = parent;
		this.node = node;
	}

	@Override
	public Void invoke() throws Exception {
		parent.addChildId(node);
		return null;
	}

	@Override
	public void rollback() throws Exception {
		parent.removeChildId(node);
	}

}
