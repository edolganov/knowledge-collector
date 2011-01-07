package ru.dolganov.tool.knowledge.collector.command;

import javax.swing.tree.DefaultMutableTreeNode;

import model.knowledge.RootElement;
import ru.dolganov.tool.knowledge.collector.command.ops.TreeOps;

public class MoveNode extends TreeCommand {

	DefaultMutableTreeNode tagretNode;
	DefaultMutableTreeNode draggedNode;
	
	public MoveNode(DefaultMutableTreeNode tagretNode,
			DefaultMutableTreeNode draggedNode) {
		super();
		this.tagretNode = tagretNode;
		this.draggedNode = draggedNode;
	}



	@Override
	public void doAction() throws Exception {		
		RootElement[] nodes = TreeOps.getMoveElements(tagretNode, draggedNode);
		if(nodes != null){
			doNextAction(new AddTreeNode(nodes[0], nodes[1]));
		}
	}

}
