package ru.kc.tools.filepersist.model.impl;

import ru.kc.model.FileLink;

public class FileLinkBean extends NodeBean implements FileLink {
	
	protected String path;

	@Override
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
