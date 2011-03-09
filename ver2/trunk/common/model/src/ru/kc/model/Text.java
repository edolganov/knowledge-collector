package ru.kc.model;

public interface Text extends Node {
	
	boolean hasText();
	
	String getText() throws Exception;
	
	String safeGetText();

}
