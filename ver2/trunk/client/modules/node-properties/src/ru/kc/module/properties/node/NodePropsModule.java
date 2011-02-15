package ru.kc.module.properties.node;

import ru.kc.model.Node;
import ru.kc.module.properties.PropsUpdater;
import ru.kc.module.properties.ui.NodeProps;
import ru.kc.platform.module.Module;


public class NodePropsModule extends Module<NodeProps> implements PropsUpdater{

	@Override
	protected NodeProps createUI() {
		return new NodeProps();
	}

	public void setNode(Node node) {
		getController(NodePropsController.class).setNode(node);
	}

	@Override
	public void enableUpdateMode() {
		getController(NodePropsController.class).enableUpdateMode();
	}

	@Override
	public void disableUpdateMode() {
		getController(NodePropsController.class).disableUpdateMode();
	}


}
