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
		if(parent.getContainer() == null) throw new IllegalStateException("parent must contains container: "+parent);
		if(node.getContainer() == null) throw new IllegalStateException("child must contains container: "+node);
		
		parent.addChildId(node);
		return null;
	}

	@Override
	public void rollback() throws Exception {
		if(parent.getContainer() == null) throw new IllegalStateException("parent must contains container: "+parent);
		if(node.getContainer() == null) throw new IllegalStateException("child must contains container: "+node);
		
		parent.removeChildId(node);
	}

}
