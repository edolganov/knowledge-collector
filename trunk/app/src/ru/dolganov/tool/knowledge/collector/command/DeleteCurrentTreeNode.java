package ru.dolganov.tool.knowledge.collector.command;

import model.knowledge.RootElement;

public class DeleteCurrentTreeNode extends TreeCommand {

	@Override
	public void doAction() {
		RootElement node = getCurNode();
		if(node == null) return;
		dao.delete(node);
	}

}
