package ru.kc.model;

public interface Text extends Node {
	
	String getText() throws Exception;
	
	String safeGetText();

}
