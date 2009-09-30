package ru.chapaj.util.ui.controller;


public abstract class GenericController<T> {
	
	public abstract void init(T ui);

	@SuppressWarnings("unchecked")
	public void initUnsaveObject(Object ui){
		init((T)ui);
	}
}
