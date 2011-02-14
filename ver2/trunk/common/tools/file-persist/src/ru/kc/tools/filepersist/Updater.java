package ru.kc.tools.filepersist;

import ru.kc.model.Node;

public interface Updater {
	
	void updateName(Node node, String name) throws Exception;
	
	void updateDescription(Node node, String description) throws Exception;
	
	void update(Node node, String name, String description) throws Exception;
	
	

}
