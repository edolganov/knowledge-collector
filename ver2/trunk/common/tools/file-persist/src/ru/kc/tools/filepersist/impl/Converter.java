package ru.kc.tools.filepersist.impl;

import ru.kc.model.Node;
import ru.kc.tools.filepersist.model.impl.NodeBean;

public class Converter {
	
	@SuppressWarnings("unchecked")
	public <T extends NodeBean> T convert(Node node) {
		if(node != null){
			if(node instanceof NodeBean) return (T) node;
			//else
			throw new IllegalArgumentException("unknow node type: "+node.getClass());
		} else {
			throw new IllegalArgumentException("node is null");
		}

	}

}
