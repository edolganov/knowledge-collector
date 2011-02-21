package ru.kc.module.properties.node;

import ru.kc.model.Node;
import ru.kc.module.properties.ui.NodeProps;
import ru.kc.platform.annotations.Mapping;

@Mapping(NodeProps.class)
public class NodePropsController extends AbstractNodePropsController<Node, NodeProps> {
	
	@Override
	protected void init() {
		init(ui.name, ui.description, ui.save, ui.revert);
	}


}
