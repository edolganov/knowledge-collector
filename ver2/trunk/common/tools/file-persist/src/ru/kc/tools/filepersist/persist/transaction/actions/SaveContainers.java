package ru.kc.tools.filepersist.persist.transaction.actions;

import java.util.HashSet;

import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.transaction.AtomicAction;

public class SaveContainers extends AtomicAction<Void>{
	
	
	HashSet<Container> containers = new HashSet<Container>();
	
	public SaveContainers(NodeBean... nodes) {
		super();
		if(nodes != null){
			for(NodeBean node : nodes){
				containers.add(node.getContainer());
			}
		}
	}
	
	@Override
	public Void invoke() throws Throwable {
		for(Container con : containers){
			t.invoke(new SaveContainer(con));
		}
		return null;
	}
	
	@Override
	public void rollback() throws Exception {}

}
