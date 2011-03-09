package ru.kc.tools.filepersist.model.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import ru.kc.model.Text;

@XStreamAlias("text")
public class TextBean extends NodeBean implements Text {

	private final static Log log = LogFactory.getLog(TextBean.class);
	
	@Override
	public boolean hasText() {
		if(container != null){
			return container.getContext().textService.hasText(this);
		} else {
			return false;
		}
	}
	
	@Override
	public String getText() throws Exception {
		if(container != null){
			return container.getContext().textService.getText(this);
		} else {
			return null;
		}
	}

	@Override
	public String safeGetText() {
		try {
			return getText();
		} catch (Exception e) {
			log.error("error while get text", e);
			return null;
		}
	}



}
