package ru.dolganov.tool.knowledge.collector.command;

import ru.chapaj.util.Check;
import model.knowledge.Link;
import model.knowledge.RootElement;

public class AddTreeNode extends TreeCommand {

	
	RootElement parent, node;
	
	
	
	public AddTreeNode(RootElement node) {
		super();
		this.parent = getCurNode();
		this.node = node;
	}

	public AddTreeNode(RootElement parent, RootElement node) {
		super();
		this.parent = parent;
		this.node = node;
	}

	@Override
	public void doAction() {
		if(parent == null || node == null) return;
		if(node instanceof Link){
			Link l = (Link) node;
			String name = l.getName();
			String url = l.getUrl();
			if(Check.isEmpty(name)){
				l.setName(url);
			}
			else if(Check.isEmpty(url)){
				l.setUrl(name);
			}
		}
		
		dao.addChild(parent, node);
	}

}
