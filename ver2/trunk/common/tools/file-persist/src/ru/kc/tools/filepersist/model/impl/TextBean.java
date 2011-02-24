package ru.kc.tools.filepersist.model.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import ru.kc.model.Text;

@XStreamAlias("text")
public class TextBean extends NodeBean implements Text {

	private final static Log log = LogFactory.getLog(TextBean.class);
	
	@Override
	public String getText() throws Exception {
		return getContainer().getContext().textService.getText(this);
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
