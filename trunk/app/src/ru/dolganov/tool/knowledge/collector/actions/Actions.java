package ru.dolganov.tool.knowledge.collector.actions;

import model.knowledge.NodeMeta;
import ru.dolganov.tool.knowledge.collector.App;
import ru.dolganov.tool.knowledge.collector.tree.TreeController;

public class Actions {
	
	public static void deleteCurrentTreeNode(){
		App.getDefault().getController(TreeController.class).deleteCurrentNode();
	}

	public static void addTreeNode(NodeMeta node) {
		App.getDefault().getController(TreeController.class).addNode(node);
		
	}

}
