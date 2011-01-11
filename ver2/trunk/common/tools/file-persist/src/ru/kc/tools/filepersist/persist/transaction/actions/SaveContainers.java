package ru.kc.tools.filepersist.persist.transaction.actions;

import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.transaction.AtomicAction;

public class SaveContainers extends AtomicAction<Void>{
	
	NodeBean node1;
	NodeBean node2;
	
	
	
	public SaveContainers(NodeBean node1, NodeBean node2) {
		super();
		this.node1 = node1;
		this.node2 = node2;
	}
	
	@Override
	public Void invoke() throws Exception {
		Container con1 = node1.getContainer();
		t.invoke(new SaveContainer(con1));
		
		Container con2 = node2.getContainer();
		if(!con1.equals(con2)){
			t.invoke(new SaveContainer(con2));
		}
		return null;
	}
	
	@Override
	public void rollback() throws Exception {}

}
