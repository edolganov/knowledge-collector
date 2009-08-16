package ru.dolganov.tool.knowledge.collector;

import java.util.HashMap;

import ru.chapaj.util.ui.controller.GenericController;
import ru.dolganov.tool.knowledge.collector.dao.DAO;

public abstract class Controller<T> extends GenericController<T> {
	
	public static HashMap<String, Controller<?>> controllers = new HashMap<String, Controller<?>>();
	
	protected DAO dao;
	
	public Controller() {
		controllers.put(this.getClass().getName(), this);
	}
	
	public <N> Controller<?> get(Class<N> clazz){
		return controllers.get(clazz.getName());
	}

	public void setDao(DAO dao) {
		this.dao = dao;
	}
	

}
