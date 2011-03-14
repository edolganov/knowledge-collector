package ru.kc.platform.action.facade;

public interface AbstractActionFacade {
	
	void enabledRequest();
	
	void disable();
	
	void setOrder(int order);
	
	int getOrder();
	
	void requestFocus();
	
	void setGroup(String group);
	
	String getGroup();

}
