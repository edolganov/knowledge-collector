package ru.kc.tools.filepersist.command;

import ru.kc.model.Dir;
import ru.kc.tools.filepersist.model.impl.NodeBean;

public class CreateOrLoadData extends Command<Void>{

	@Override
	public Void invoke() throws Exception {
		NodeBean node = c.fs.getRoot();
		if(node == null){
			Dir dir = c.dataFactory.createDir("root");
			NodeBean root = c.convertorService.convert(dir);
			c.fs.createRoot(root);
		}
		
		return null;
	}
	
	

}
