package ru.kc.tools.filepersist.persist.transaction.actions;

import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.transaction.AtomicAction;

public class RemoveNodeFromContainer extends AtomicAction<Void>{
	
	NodeBean node;
	Container container;

	public RemoveNodeFromContainer(NodeBean node) {
		super();
		this.node = node;
		this.container = node.getContainer();
	}

	@Override
	public Void invoke() throws Exception {
		container.remove(node);
		node.setContainer(null);
		
		return null;
	}

	@Override
	public void rollback() throws Exception {
		container.add(node);
		node.setContainer(container);
	}

}
