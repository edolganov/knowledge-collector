package ru.kc.tools.filepersist.persist.model;

import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.util.collection.LimitedList;
import ru.kc.util.collection.TreeList;

public class ContainersModel {
	
	private TreeList<LimitedList<Container>> tree = new TreeList<LimitedList<Container>>();
	
	public void setRoot(Container container){
		if(tree.size() > 0) throw new IllegalStateException("tree is not empty");
		
		LimitedList<Container> pool = LimitedList.create(container);
		tree.add(pool);
	}

	public Container getRoot() {
		LimitedList<Container> pool = tree.getRoot();
		if(!pool.isEmpty()) {
			Container container = pool.get(0);
			return container;
		} else {
			throw new IllegalStateException("no root in model");
		}
	}

	public Container getNotFullContainer() {
		// TODO Auto-generated method stub
		return null;
	}

}
