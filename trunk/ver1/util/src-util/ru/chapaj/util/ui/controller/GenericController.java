package ru.chapaj.util.ui.controller;


public abstract class GenericController<T> {
	
	public abstract void init(T ui);
	
	/**
	 * Метод, который вызывается после того, как все контроллеры были инициализированны
	 */
	public void afterAllInit(){/*override if need*/};

	@SuppressWarnings("unchecked")
	public void initUnsaveObject(Object ui){
		init((T)ui);
	}
}
