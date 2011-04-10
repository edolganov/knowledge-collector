package ru.kc.module.imports.oldclient.oldmodel;

public abstract class Link extends OldNode {
	
	String url;
	
	public Link() {
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String toString() {
		return name != null ? name : url;
	}
	

}
