package ru.kc.tools.filepersist.impl;

import java.util.Arrays;
import java.util.Collection;

import ru.kc.model.Node;
import ru.kc.tools.filepersist.UpdateBuilder;
import ru.kc.tools.filepersist.Updater;
import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.FileSystemImpl;
import ru.kc.tools.filepersist.update.UpdateDescription;
import ru.kc.tools.filepersist.update.UpdateName;
import ru.kc.tools.filepersist.update.UpdateRequest;

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
	public UpdateBuilder builder() {
		return new UpdateBuilderImpl();
	}
	
	@Override
	public void update(Node node, UpdateRequest... updates) throws Exception {
		if(updates != null){
			update(node, Arrays.asList(updates));
		}
	}

	@Override
	public void update(Node node, Collection<UpdateRequest> updates) throws Exception {
		NodeBean old = convert(node);
		NodeBean clone = (NodeBean)old.clone();
		applyUpdates(clone, updates);

		fs.replace(old,clone);
		listeners.fireUpdatedEvent(old, clone);
	}
	


	
	private void applyUpdates(NodeBean clone, Collection<UpdateRequest> updates) {
		for (UpdateRequest update : updates) {
			if(update instanceof UpdateName){
				String name = ((UpdateName) update).value;
				clone.setName(name);
			} 
			else if(update instanceof UpdateDescription){
				String description = ((UpdateDescription) update).value;
				clone.setDescription(description);
			}
		}
	}


	private NodeBean convert(Node node) {
		return c.converter.convert(node);
	}






}
