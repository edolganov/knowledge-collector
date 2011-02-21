package ru.kc.tools.filepersist.impl;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.model.Node;
import ru.kc.tools.filepersist.UpdateBuilder;
import ru.kc.tools.filepersist.Updater;
import ru.kc.tools.filepersist.model.impl.LinkBean;
import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.FileSystemImpl;
import ru.kc.tools.filepersist.update.UpdateDescription;
import ru.kc.tools.filepersist.update.UpdateName;
import ru.kc.tools.filepersist.update.UpdateRequest;
import ru.kc.tools.filepersist.update.UpdateUrl;

public class UpdaterImpl implements Updater {
	
	private static Log log = LogFactory.getLog(UpdaterImpl.class);
	
	Context c;
	FileSystemImpl fs;
	Listeners listeners;
	
	public void init(Context context){
		c = context;
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
	


	
	private void applyUpdates(NodeBean node, Collection<UpdateRequest> updates) {
		for (UpdateRequest update : updates) {
			if(update instanceof UpdateName){
				String name = ((UpdateName) update).value;
				node.setName(name);
			} 
			else if(update instanceof UpdateDescription){
				String description = ((UpdateDescription) update).value;
				node.setDescription(description);
			}
			else if(update instanceof UpdateUrl){
				if(node instanceof LinkBean){
					String url = ((UpdateUrl) update).value;
					((LinkBean)node).setUrl(url);
				} else {
					log.error("can't update url for "+node);
				}
			}
		}
	}


	private NodeBean convert(Node node) {
		return c.converter.convert(node);
	}






}
