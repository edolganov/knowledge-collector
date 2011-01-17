package ru.kc.tools.filepersist;

import java.util.List;

import ru.kc.model.Node;

public interface Tree {
	
	Node getRoot() throws Exception;
	
	List<Node> getChildren(Node node) throws Exception;
	
	void add(Node parent, Node node) throws Exception;

}
