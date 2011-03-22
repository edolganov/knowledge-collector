package ru.kc.common.tree;

public interface TreeNodeFacade {

	<T> T getUserObject(Class<T> type);
	
	boolean isOpen();
	
}
