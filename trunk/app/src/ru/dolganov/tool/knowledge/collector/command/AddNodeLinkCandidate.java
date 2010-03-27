package ru.dolganov.tool.knowledge.collector.command;

import ru.dolganov.tool.knowledge.collector.App;

public class AddNodeLinkCandidate extends TreeCommand {

	@Override
	void doAction() throws Exception {
		App.getDefault().putSessionObj("node-link-candidate", getTreeNode());
	}

}
