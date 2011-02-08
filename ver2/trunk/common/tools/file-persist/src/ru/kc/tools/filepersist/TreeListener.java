package ru.kc.tools.filepersist;

import ru.kc.model.Node;

public interface TreeListener {
	
	void onAdded(Node parent, Node child);

}
