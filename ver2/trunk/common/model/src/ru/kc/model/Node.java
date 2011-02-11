package ru.kc.model;

import java.util.List;

public interface Node {
	
	String getId();
	
	String getName();
	
	String getDescription();
	
	Long getCreateDate();
	
	Node getParent() throws Exception;
	
	List<Node> getChildren() throws Exception;


}
