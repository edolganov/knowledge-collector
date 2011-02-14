package ru.kc.tools.filepersist.persist.transaction.actions;

import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.transaction.AtomicAction;

public class ReplaceNodeInContainer extends AtomicAction<Void>{
	
	NodeBean old;
	NodeBean newNode;
	Container container;

	public ReplaceNodeInContainer(NodeBean old, NodeBean newNode) {
		super();
		this.old = old;
		this.container = old.getContainer();
		this.newNode = newNode;

	}

	@Override
	public Void invoke() throws Exception {
		container.remove(old);
		container.add(newNode);
		return null;
	}

	@Override
	public void rollback() throws Exception {
		container.remove(newNode);
		container.add(old);
		
	}

}
