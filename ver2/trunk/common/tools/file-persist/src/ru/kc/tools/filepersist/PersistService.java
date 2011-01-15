package ru.kc.tools.filepersist;

import java.util.Collection;

import ru.kc.model.Node;

public interface PersistService {
	
	Node getRoot() throws Exception;
	
	Collection<Node> getChildren(Node node) throws Exception;

}
