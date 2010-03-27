package ru.dolganov.tool.knowledge.collector.command;

import javax.swing.tree.DefaultMutableTreeNode;

import model.knowledge.NodeLink;
import model.knowledge.RootElement;
import ru.dolganov.tool.knowledge.collector.App;
import ru.dolganov.tool.knowledge.collector.command.ops.TreeOps;

public class CreateNodeLink extends TreeCommand {

	@Override
	void doAction() throws Exception {
		RootElement[] nodes = TreeOps.getMoveElements(getTreeNode(), (DefaultMutableTreeNode)App.getDefault().getSessionObject("node-link-candidate"));
		if(nodes != null){
			RootElement parent = nodes[0];
			RootElement donor = nodes[1];
			NodeLink link = new NodeLink();
			link.setNodeUuid(donor.getUuid());
			doNextAction(new AddTreeNode(parent, link));
		}
	}

}
