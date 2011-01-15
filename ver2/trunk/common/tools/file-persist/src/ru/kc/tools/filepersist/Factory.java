package ru.kc.tools.filepersist;

import ru.kc.model.Dir;
import ru.kc.model.FileLink;
import ru.kc.model.Link;
import ru.kc.model.Text;

public interface Factory {
	
	Dir createDir(String name);
	
	Text createText(String name);
	
	Link createLink(String name);
	
	FileLink createFileLink(String name);

}
