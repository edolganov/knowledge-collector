package ru.kc.tools.filepersist;

import java.util.Collection;

import ru.kc.model.Node;
import ru.kc.tools.filepersist.update.UpdateRequest;

public interface Updater {
	
	void update(Node node, UpdateRequest... updates) throws Exception;
	
	void update(Node node, Collection<UpdateRequest> updates) throws Exception;

}
