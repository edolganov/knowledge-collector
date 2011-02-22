package ru.kc.tools.filepersist.impl;

import ru.kc.model.Text;
import ru.kc.tools.filepersist.TextService;
import ru.kc.tools.filepersist.model.impl.TextBean;
import ru.kc.tools.filepersist.persist.FileSystemImpl;

public class TextServiceImpl implements TextService {

	Context c;
	FileSystemImpl fs;
	Listeners listeners;

	public void init(Context context) {
		c = context;
		fs = context.fs;
		listeners = context.listeners;
		
	}
	
	@Override
	public String getText(Text text) throws Exception {
		TextBean textBean = convert(text);
		if(textBean.getContainer() == null) 
			throw new IllegalArgumentException("not saved node");
		return fs.getText(textBean);
	}
	
	@Override
	public void setText(Text text, String content) throws Exception {
		if(content == null)
			throw new IllegalArgumentException("null content");
		
		TextBean textBean = convert(text);
		if(textBean.getContainer() == null) 
			throw new IllegalArgumentException("not saved node");
		
		fs.setText(textBean, content);
	}
	
	
	private TextBean convert(Text node) {
		return c.converter.convert(node);
	}



}
