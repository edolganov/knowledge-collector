package ru.kc.module.properties.node;

import ru.kc.module.properties.ui.NodeProps;
import ru.kc.platform.annotations.GlobalMapping;
import ru.kc.platform.module.Module;


public class NodePropsModule extends Module<NodeProps>{

	@Override
	protected NodeProps createUI() {
		return new NodeProps();
	}

}
