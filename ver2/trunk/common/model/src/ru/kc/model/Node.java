package ru.kc.model;

import java.util.List;

public interface Node {
	
	String getId();
	
	String getName();
	
	String getDescription();
	
	List<Node> getChildren() throws Exception;
	
	Long getCreateDate();

}
