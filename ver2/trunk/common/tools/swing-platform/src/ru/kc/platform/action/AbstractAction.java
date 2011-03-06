package ru.kc.platform.action;

import java.awt.Component;

public abstract class AbstractAction {
	
	private int order;
	
	public abstract Component getComponent();
	
	public void setOrder(int order){
		this.order = order;
	}
	
	public int getOrder(){
		return order;
	}

}
