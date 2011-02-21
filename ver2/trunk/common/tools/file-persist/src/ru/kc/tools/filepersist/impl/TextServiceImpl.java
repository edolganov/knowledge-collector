package ru.kc.tools.filepersist.impl;

import ru.kc.model.Text;
import ru.kc.tools.filepersist.TextService;
import ru.kc.tools.filepersist.persist.FileSystemImpl;

public class TextServiceImpl implements TextService {

	FileSystemImpl fs;
	Listeners listeners;

	public void init(Context context) {
		fs = context.fs;
		listeners = context.listeners;
		
	}
	
	@Override
	public String getText(Text text) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
