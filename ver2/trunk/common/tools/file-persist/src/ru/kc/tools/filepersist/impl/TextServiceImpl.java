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
		checkContainer(textBean);
		return fs.getText(textBean);
	}
	
	public void setText(Text text, String content) throws Exception {
		if(content == null)
			throw new IllegalArgumentException("null content");
		
		TextBean textBean = convert(text);
		checkContainer(textBean);
		
		fs.setText(textBean, content);
	}


	public void removeText(Text text) throws Exception {
		TextBean textBean = convert(text);
		checkContainer(textBean);
		
		fs.removeText(textBean);
	}
	
	
	private void checkContainer(TextBean textBean) {
		if(textBean.getContainer() == null) 
			throw new IllegalArgumentException("not saved node");
	}
	
	private TextBean convert(Text node) {
		return c.converter.convert(node);
	}





}
