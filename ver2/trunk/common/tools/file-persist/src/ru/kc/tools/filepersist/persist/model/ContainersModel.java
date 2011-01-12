package ru.kc.tools.filepersist.persist.model;

import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.util.collection.LimitedLevelTreeList;
import ru.kc.util.collection.LimitedList;

public class ContainersModel {
	
	private LimitedLevelTreeList<LimitedList<Container>> limitedLevelTreeList = new LimitedLevelTreeList<LimitedList<Container>>();
	
	public void setRoot(Container container){
		if(limitedLevelTreeList.size() > 0) throw new IllegalStateException("tree is not empty");
		
		LimitedList<Container> pool = LimitedList.create(container);
		limitedLevelTreeList.add(pool);
	}

	public Container getRoot() {
		LimitedList<Container> pool = limitedLevelTreeList.getRoot();
		if(!pool.isEmpty()) {
			Container container = pool.get(0);
			return container;
		} else {
			throw new IllegalStateException("no root in model");
		}
	}

	public Container getNotFullContainer() {
		LimitedList<Container> notFullList = null;
		for (LimitedList<Container> list : limitedLevelTreeList) {
			if(!list.isFull()){
				notFullList = list;
				break;
			}
		}
		return notFullList != null? notFullList.getLast() : null;
	}

}
