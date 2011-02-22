package ru.kc.tools.filepersist;

import ru.kc.model.Dir;
import ru.kc.model.FileLink;
import ru.kc.model.Link;
import ru.kc.model.Text;

public interface Factory {
	
	Dir createDir(String name, String description);
	
	Text createText(String name, String description);
	
	Link createLink(String name, String url, String description);
	
	FileLink createFileLink(String name, String path, String description);

}
