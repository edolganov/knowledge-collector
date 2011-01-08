package ru.kc.tools.filepersist.model.impl;

import ru.kc.util.collection.LimitedList;
import ru.kc.util.collection.TreeList;

public class ContainersTree {
	
	private TreeList<LimitedList<Container>> tree = new TreeList<LimitedList<Container>>();
	
	public void setRoot(Container container){
		if(tree.size() > 0) throw new IllegalStateException("tree is not empty");
		
		LimitedList<Container> pool = LimitedList.create(container);
		tree.add(pool);
	}

	public Container getRoot() {
		LimitedList<Container> pool = tree.getRoot();
		if(pool.isEmpty()) {
			return null;
		} else {
			Container container = pool.get(0);
			return container;
		}
	}

}
