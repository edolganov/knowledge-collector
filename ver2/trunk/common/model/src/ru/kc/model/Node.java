package ru.kc.model;

import java.util.Collection;

public interface Node {
	
	String getId();
	
	String getName();
	
	String getDescription();
	
	Collection<Node> getChildren();
	
	Long getCreateDate();

}
