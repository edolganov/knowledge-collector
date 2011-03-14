package ru.kc.platform.action.facade;

import java.util.Comparator;

public class ActionFacadeComparator implements Comparator<AbstractActionFacade>{

	@Override
	public int compare(AbstractActionFacade a, AbstractActionFacade b) {
		String groupA = a.getGroup();
		String groupB = b.getGroup();
		if(groupA == null && groupB == null)
			return compareIndexes(a, b);
		
		if(groupA == null && groupB != null)
			return -1;
		
		if(groupA != null && groupB == null)
			return 1;
		
		if(groupA.equals(groupB)){
			return compareIndexes(a, b);
		} else {
			return groupA.compareTo(groupB);
		}
		
	}
	
	private int compareIndexes(AbstractActionFacade a, AbstractActionFacade b){
		int thisVal = a.getOrder();
		int anotherVal = b.getOrder();
		return (thisVal<anotherVal ? -1 : (thisVal==anotherVal ? 0 : 1));
	}

}
