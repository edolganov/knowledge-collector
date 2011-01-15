package ru.kc.tools.filepersist.model.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import ru.kc.model.Text;

@XStreamAlias("text")
public class TextBean extends NodeBean implements Text {
	
	private String textToSave;

	@Override
	public String getText() {
		return null;
	}

	public void setTextToSave(String textToSave) {
		this.textToSave = textToSave;
	}

}
