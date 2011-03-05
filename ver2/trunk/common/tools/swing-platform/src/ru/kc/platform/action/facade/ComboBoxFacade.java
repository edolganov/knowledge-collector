package ru.kc.platform.action.facade;

import java.util.List;



public interface ComboBoxFacade extends AbstractActionFacade {
	
	public static interface Listener {
		
		void onSelected(Object value);
		
	}
	
	void setValues(List<Object> values);
	
	void selectValue(int index);
	
	void addListener(Listener listener);
	

}
