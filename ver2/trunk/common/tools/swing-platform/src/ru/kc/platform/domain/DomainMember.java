package ru.kc.platform.domain;

public interface DomainMember {
	
	public static final Object ROOT_DOMAIN_KEY = new Object(){
		public String toString() {
			return "ROOT_DOMAIN_KEY";
		};
	};
	
	Object getDomainKey();

}
