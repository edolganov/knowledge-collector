package ru.kc.tools.filepersist;

import java.util.List;

import ru.kc.model.Node;

public interface Tree {
	
	Node getRoot() throws Exception;
	
	List<Node> getChildren(Node node) throws Exception;
	
	void add(Node parent, Node node) throws Exception;

	void deleteRecursive(Node node)throws Exception;
	
	boolean canMove(Node child, Node newParent) throws Exception;
	
	void move(Node child, Node newParent) throws Exception;
	
	int moveUp(Node node) throws Exception;
	
	int moveDown(Node node) throws Exception;

}
