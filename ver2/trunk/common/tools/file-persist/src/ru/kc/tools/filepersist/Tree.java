package ru.kc.tools.filepersist;

import java.util.List;

import ru.kc.model.Node;

public interface Tree {
	
	void addListener(TreeListener listener);
	
	Node getRoot() throws Exception;
	
	List<Node> getChildren(Node node) throws Exception;
	
	void add(Node parent, Node node) throws Exception;

	void deleteRecursive(Node node)throws Exception;

}
