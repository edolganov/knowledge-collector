package ru.kc.tools.filepersist.impl;

import ru.kc.model.Node;
import ru.kc.tools.filepersist.model.impl.NodeBean;

public class Converter {
	
	public NodeBean convert(Node node) {
		if(node != null){
			if(node instanceof NodeBean) return (NodeBean) node;
			//else
			throw new IllegalArgumentException("unknow node type: "+node.getClass());
		} else {
			throw new IllegalArgumentException("node is null");
		}

	}

}
