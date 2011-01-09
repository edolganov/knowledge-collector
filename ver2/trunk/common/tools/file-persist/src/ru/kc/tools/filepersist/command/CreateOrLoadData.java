package ru.kc.tools.filepersist.command;

import ru.kc.tools.filepersist.model.impl.NodeBean;

public class CreateOrLoadData extends Command<Void>{

	@Override
	public Void invoke() throws Exception {
		NodeBean node = c.fs.getRoot();
		if(node == null){
			NodeBean root = c.dataFactory.createDir("root");
			c.fs.createRoot(root);
		}
		
		return null;
	}
	
	

}
