package ru.kc.tools.filepersist;

import ru.kc.model.Text;

public interface TextService {
	
	String getText(Text text) throws Exception;
	
	void setText(Text text, String content) throws Exception;
	
	void removeText(Text text) throws Exception;

}
