package ru.kc.platform.action.facade;

public interface AbstractActionFacade {
	
	void enabledRequest();
	
	void disable();
	
	void setOrder(int order);
	
	int getOrder();

}
