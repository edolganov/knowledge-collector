package ru.kc.platform.action.facade;

import java.util.Comparator;

public class ActionFacadeComparator implements Comparator<AbstractActionFacade>{

	@Override
	public int compare(AbstractActionFacade a, AbstractActionFacade b) {
		int thisVal = a.getOrder();
		int anotherVal = b.getOrder();
		return (thisVal<anotherVal ? -1 : (thisVal==anotherVal ? 0 : 1));
	}

}
