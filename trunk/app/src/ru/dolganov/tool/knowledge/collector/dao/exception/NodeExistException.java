package ru.dolganov.tool.knowledge.collector.dao.exception;

public class NodeExistException extends RuntimeException{

	String name;
	
	public String getName() {
		return name;
	}

	public NodeExistException(String name) {
		super();
		this.name = name;
	}



}
