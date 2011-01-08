package ru.kc.tools.filepersist.model.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import ru.kc.model.Link;

@XStreamAlias("link")
public class LinkBean extends NodeBean implements Link {
	
	protected String url;

	@Override
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
