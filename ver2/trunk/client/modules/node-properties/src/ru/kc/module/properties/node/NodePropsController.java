package ru.kc.module.properties.node;

import ru.kc.common.controller.Controller;
import ru.kc.model.Node;
import ru.kc.module.properties.tools.EmptyTextAreaDecorator;
import ru.kc.module.properties.ui.NodeProps;
import ru.kc.platform.annotations.Mapping;

@Mapping(NodeProps.class)
public class NodePropsController extends Controller<NodeProps>{

	EmptyTextAreaDecorator desctiptionDecorator;
	
	@Override
	protected void init() {
		desctiptionDecorator = new EmptyTextAreaDecorator(ui.description);
	}
	
	public void setNode(Node node){
		ui.name.setText(node.getName());
		ui.description.setText(node.getDescription());
		desctiptionDecorator.fillBackground();
	}
	
	

}
