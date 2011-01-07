package ru.kc.tools.filepersist.command;

import ru.kc.tools.filepersist.model.impl.NodeBean;

public class CreateOrLoadData extends Command<Void>{

	@Override
	public Void invoke() throws Exception {
		NodeBean node = c.persistManager.getRootNode();
		if(node == null){
			NodeBean root = c.dataFactory.createDir(null,"root");
			c.persistManager.saveNode(root);
		}
		
		return null;
	}
	
	

}
