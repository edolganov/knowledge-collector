package ru.kc.platform.domain;

public interface DomainMember {
	
	public static final Object ROOT_DOMAIN = new Object();
	
	Object getDomainKey();

}
