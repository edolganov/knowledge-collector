package ru.kc.tools.filepersist.persist.transaction.actions;

import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.transaction.AtomicAction;

public class AddNodeToContainer extends AtomicAction<Void>{
	
	NodeBean node;
	Container container;

	public AddNodeToContainer(NodeBean node, Container container) {
		super();
		this.node = node;
		this.container = container;
	}

	@Override
	public Void invoke() throws Exception {
		container.add(node);
		node.setContainer(container);
		return null;
	}

	@Override
	public void rollback() throws Exception {
		container.remove(node);
		node.setContainer(null);
		
	}

}
