package ru.kc.tools.filepersist.persist.transaction.actions;

import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.transaction.AtomicAction;
import ru.kc.util.collection.Pair;

public class GetParent extends AtomicAction<NodeBean>{
	
	NodeBean child;
	
	

	public GetParent(NodeBean child) {
		super();
		this.child = child;
	}

	@Override
	protected NodeBean invoke() throws Throwable {
		String parentPathAndId = child.getParentId();
		if(parentPathAndId == null) return null;
		
		Pair<String, String> data = child.parse(parentPathAndId);
		String simpleFilePath = data.getFirst();
		Container container = c.containerModel.getContainer(simpleFilePath);
		if(container == null) 
			throw new IllegalStateException("container not found by path "+simpleFilePath);
		
		String id = data.getSecond();
		NodeBean parent = container.find(id);
		return parent;
	}

	@Override
	protected void rollback() throws Throwable {}

}
