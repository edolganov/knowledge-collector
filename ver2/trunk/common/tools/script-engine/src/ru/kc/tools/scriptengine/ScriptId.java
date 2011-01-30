package ru.kc.tools.scriptengine;

public class ScriptId {
	
	private static final Object defaultDomain = new Object();
	
	public final Object domain;
	public final Object uniqueName;
	
	public ScriptId(Object uniqueName) {
		this(defaultDomain, uniqueName);
	}
	
	public ScriptId(Object domain, Object uniqueName) {
		super();
		if(domain == null) throw new IllegalArgumentException("domain is null");
		if(uniqueName == null) throw new IllegalArgumentException("uniqueName is null");
		
		this.domain = domain;
		this.uniqueName = uniqueName;
	}

	@Override
	public String toString() {
		return "ScriptId [domain=" + domain + ", uniqueName=" + uniqueName
				+ "]";
	}

}
