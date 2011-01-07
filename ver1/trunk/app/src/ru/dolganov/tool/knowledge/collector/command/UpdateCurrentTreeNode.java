package ru.dolganov.tool.knowledge.collector.command;

import java.util.Map;

import model.knowledge.RootElement;

public class UpdateCurrentTreeNode extends TreeCommand {

	
	Map<String, String> params;
	
	public UpdateCurrentTreeNode(Map<String, String> params) {
		super();
		this.params = params;
	}



	@Override
	public void doAction() throws Exception {
		RootElement node = getCurNode();
		if(node == null) return;
		dao.update(node,params);
	}

}
