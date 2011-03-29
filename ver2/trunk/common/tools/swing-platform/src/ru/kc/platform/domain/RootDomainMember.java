package ru.kc.platform.domain;

public class RootDomainMember implements DomainMember {
	
	Object owner;

	public RootDomainMember(Object owner) {
		super();
		this.owner = owner;
	}

	@Override
	public Object getDomainKey() {
		return DomainMember.ROOT_DOMAIN_KEY;
	}

	@Override
	public String toString() {
		return "RootDomainMember [owner=" + owner + "]";
	}

}
