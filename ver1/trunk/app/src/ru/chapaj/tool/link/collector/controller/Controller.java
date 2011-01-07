package ru.chapaj.tool.link.collector.controller;

import java.util.Properties;

import ru.chapaj.tool.link.collector.App;
import ru.chapaj.tool.link.collector.model.DataContainer;

public abstract class Controller<T> {
	
	public abstract void init(T ui);
	
	/**
	 * Обработать глобальные настройки приложения
	 * @param props
	 */
	public void processProps(Properties props){}
	
	/**
	 * данные были обновлены
	 * @param data
	 */
	public void processNewData(DataContainer data){}
	
	/**
	 * внести изменения в данные
	 * @param data
	 * @param full TODO
	 */
	public void refreshData(DataContainer data, boolean full){}
	
	
	protected void fileIsModified(){
		App.getDefault().informer().fileIsModified();
		App.getDefault().getMainController().actionSaveFile(false);
	}
	
	protected DataContainer getData(){
		return App.getDefault().getData();
	}

}
