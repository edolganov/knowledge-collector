package ru.kc.platform.domain;

import java.awt.Component;

import ru.kc.platform.domain.annotation.DomainSpecific;

public class DomainUtil {
	
	public static Object findDomainKey(Component component){
		DomainMember parentMember = null;
		Component curComponent = component;
		while(curComponent != null){
			if(curComponent instanceof DomainMember){
				parentMember = (DomainMember)curComponent;
				break;
			} else {
				curComponent = curComponent.getParent();
			}
		}
		
		if(parentMember != null){
			return parentMember.getDomainKey();
		} else {
			return DomainMember.ROOT_DOMAIN;
		}
		
	}

	public static boolean isDomainSpecific(Object ob) {
		DomainSpecific domainSpecificFlag = ob.getClass().getAnnotation(DomainSpecific.class);
		return domainSpecificFlag != null;
	}

}
