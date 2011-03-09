package ru.kc.tools.filepersist;

import ru.kc.model.Text;

public interface TextService {
	
	boolean hasText(Text text);
	
	String getText(Text text) throws Exception;
	
}
