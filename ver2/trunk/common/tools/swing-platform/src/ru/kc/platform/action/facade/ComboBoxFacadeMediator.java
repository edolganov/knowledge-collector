package ru.kc.platform.action.facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComboBoxFacadeMediator implements ComboBoxFacade {
	
	private List<Object> values;
	private int selectIndex;
	private ArrayList<Listener> listeners = new ArrayList<Listener>();
	private List<ComboBoxFacade> realFacadeImpls = Collections.emptyList();

	@Override
	public void enabledRequest() {
		for (ComboBoxFacade realImpl : realFacadeImpls)
			realImpl.enabledRequest();
	}

	@Override
	public void disable() {
		for (ComboBoxFacade realImpl : realFacadeImpls)
			realImpl.disable();
	}
	
	@Override
	public void setValues(List<Object> values) {
		this.values = values;
		for (ComboBoxFacade realImpl : realFacadeImpls)
			realImpl.setValues(values);
	}
	
	@Override
	public void addListener(Listener listener) {
		listeners.add(listener);
		for (ComboBoxFacade realImpl : realFacadeImpls)
			realImpl.addListener(listener);
	}
	
	@Override
	public void selectValue(int index) {
		this.selectIndex = index;
		for (ComboBoxFacade realImpl : realFacadeImpls)
			realImpl.selectValue(index);
	}

	
	public void addRealFacade(ComboBoxFacade facade){
		realFacadeImpls = new ArrayList<ComboBoxFacade>(realFacadeImpls);
		init(facade);
		realFacadeImpls.add(facade);
	}

	private void init(ComboBoxFacade facade) {
		facade.setValues(values);
		facade.selectValue(selectIndex);
		for(Listener l : listeners)
			facade.addListener(l);
	}







}
