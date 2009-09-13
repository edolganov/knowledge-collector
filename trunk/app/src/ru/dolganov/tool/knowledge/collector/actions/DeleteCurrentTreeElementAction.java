package ru.dolganov.tool.knowledge.collector.actions;

import ru.dolganov.tool.knowledge.collector.App;
import ru.dolganov.tool.knowledge.collector.tree.TreeController;

public class DeleteCurrentTreeElementAction {
	
	public DeleteCurrentTreeElementAction() {
		App.getDefault().getController(TreeController.class).deleteCurrentNode();
	}

}
