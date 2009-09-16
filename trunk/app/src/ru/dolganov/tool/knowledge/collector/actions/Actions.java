package ru.dolganov.tool.knowledge.collector.actions;

import java.util.Map;

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

	public static void updateCurrentTreeNode(Map<String, String> params) {
		App.getDefault().getController(TreeController.class).updateCurrentNode(params);
	}

}
