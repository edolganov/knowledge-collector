package ru.dolganov.tool.knowledge.collector;

import ru.chapaj.util.ui.controller.GenericController;
import ru.dolganov.tool.knowledge.collector.dao.DAO;

public abstract class Controller<T> extends GenericController<T> {
	
	protected DAO dao;

	public void setDao(DAO dao) {
		this.dao = dao;
	}
	

}
