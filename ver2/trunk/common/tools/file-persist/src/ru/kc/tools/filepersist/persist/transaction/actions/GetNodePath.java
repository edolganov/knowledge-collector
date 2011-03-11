package ru.kc.tools.filepersist.persist.transaction.actions;

import java.util.ArrayList;

import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.transaction.AtomicAction;

public class GetNodePath extends AtomicAction<NodeBean[]>{

	NodeBean node;
	
	public GetNodePath(NodeBean node) {
		super();
		this.node = node;
	}

	@Override
	protected NodeBean[] invoke() throws Throwable {
		
		ArrayList<NodeBean> path = new ArrayList<NodeBean>();
		NodeBean parent = t.invoke(new GetParent(node));
		while(parent != null){
			path.add(0, parent);
			parent = t.invoke(new GetParent(parent));
		}
		
		return path.toArray(new NodeBean[0]);
	}

	@Override
	protected void rollback() throws Throwable {}

}
