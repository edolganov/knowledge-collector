package ru.kc.tools.filepersist.impl;

import ru.kc.model.Node;
import ru.kc.tools.filepersist.Updater;
import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.FileSystemImpl;

public class UpdaterImpl implements Updater {
	
	Context c;
	TreeImpl tree;
	FileSystemImpl fs;
	Listeners listeners;
	
	public void init(Context context){
		c = context;
		tree = context.tree;
		fs = context.fs;
		listeners = c.listeners;
	}

	@Override
	public void updateName(Node node, String name) throws Exception {
		checkName(name);

		NodeBean old = convert(node);
		NodeBean clone = (NodeBean)old.clone();
		clone.setName(name);
		
		fs.replace(old,clone);
		listeners.fireUpdatedEvent(old, clone);
	}
	
	@Override
	public void updateDescription(Node node, String description)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Node node, String name, String description) {
		// TODO Auto-generated method stub

	}
	


	
	private NodeBean convert(Node node) {
		return c.converter.convert(node);
	}
	
	private void checkName(String name) {
		if(name == null) throw new IllegalArgumentException("name is null");
	}



}
