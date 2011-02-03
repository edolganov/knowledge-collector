package ru.kc.platform.scripts.controller;

public abstract class ScriptController<T> {
	
	protected T ui;
	
	public void init(T ui){
		this.ui = ui;
	}
	
	public abstract void init(); 

}
