package ru.chapaj.tool.link.collector.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("link")
public class Link extends NodeMeta {
	
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
