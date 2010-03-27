package model.knowledge;

public abstract class Link extends Node {
	
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
