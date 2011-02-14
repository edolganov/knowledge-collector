package ru.kc.tools.filepersist;

import ru.kc.model.Node;

public interface Updater {
	
	void updateNode(Node node, String name);
	
	void updateNode(Node node, String name, String description);
	
	

}
