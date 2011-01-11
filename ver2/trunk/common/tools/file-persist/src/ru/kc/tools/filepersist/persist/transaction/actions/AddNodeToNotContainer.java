package ru.kc.tools.filepersist.persist.transaction.actions;

import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.transaction.AtomicAction;

public class AddNodeToNotContainer extends AtomicAction<Void>{
	
	NodeBean node;
	Container container;

	public AddNodeToNotContainer(NodeBean node, Container container) {
		super();
		this.node = node;
		this.container = container;
	}

	@Override
	public Void invoke() throws Exception {
		container.add(node);
		return null;
	}

	@Override
	public void rollback() throws Exception {
		container.remove(node);
		
	}

}
