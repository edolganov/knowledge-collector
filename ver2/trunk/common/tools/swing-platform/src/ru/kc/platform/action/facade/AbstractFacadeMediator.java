package ru.kc.platform.action.facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public abstract class AbstractFacadeMediator<T extends AbstractActionFacade> implements AbstractActionFacade {
	
	private int order;
	private String group;
	protected List<T> realFacadeImpls = Collections.emptyList();

	@Override
	public void enabledRequest() {
		for (T realImpl : realFacadeImpls)
			realImpl.enabledRequest();
	}

	@Override
	public void disable() {
		for (T realImpl : realFacadeImpls)
			realImpl.disable();
	}
	
	@Override
	public void requestFocus() {
		for (T realImpl : realFacadeImpls)
			realImpl.requestFocus();
	}
	
	@Override
	public void setOrder(int order) {
		this.order = order;
		for (T realImpl : realFacadeImpls)
			realImpl.setOrder(order);
	}
	
	@Override
	public int getOrder() {
		return order;
	}
	
	@Override
	public void setGroup(String group) {
		this.group = group;
		for (T realImpl : realFacadeImpls)
			realImpl.setGroup(group);
		
	}

	@Override
	public String getGroup() {
		return group;
	}
	
	public void addRealFacade(T facade){
		realFacadeImpls = new ArrayList<T>(realFacadeImpls);
		basicInit(facade);
		realFacadeImpls.add(facade);
	}

	private void basicInit(T facade) {
		facade.setOrder(order);
		init(facade);
	}
	
	protected abstract void init(T facade);









}
