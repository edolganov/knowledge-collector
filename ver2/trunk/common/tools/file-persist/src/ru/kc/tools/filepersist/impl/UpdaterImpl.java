package ru.kc.tools.filepersist.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import ru.kc.model.Node;
import ru.kc.tools.filepersist.Updater;
import ru.kc.tools.filepersist.model.impl.FileLinkBean;
import ru.kc.tools.filepersist.model.impl.LinkBean;
import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.model.impl.TextBean;
import ru.kc.tools.filepersist.persist.FileSystemImpl;
import ru.kc.tools.filepersist.persist.transaction.UserTransaction;
import ru.kc.tools.filepersist.update.RemoveProperty;
import ru.kc.tools.filepersist.update.SetProperty;
import ru.kc.tools.filepersist.update.UpdateDescription;
import ru.kc.tools.filepersist.update.UpdateName;
import ru.kc.tools.filepersist.update.UpdatePath;
import ru.kc.tools.filepersist.update.UpdateRequest;
import ru.kc.tools.filepersist.update.UpdateText;
import ru.kc.tools.filepersist.update.UpdateUrl;
import ru.kc.util.Check;
import ru.kc.util.collection.Pair;

public class UpdaterImpl implements Updater {
	
	Context c;
	FileSystemImpl fs;
	Listeners listeners;
	
	public void init(Context context){
		c = context;
		fs = context.fs;
		listeners = c.listeners;
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
		
		UserTransaction userTransaction = new UserTransaction();
		try {
			userTransaction.begin();
			
			applyUpdates(clone, updates);
			fs.replace(old,clone);
			applyExternalUpdates(clone, updates);
			
			userTransaction.commit();
		}catch (Throwable e) {
			userTransaction.rollback();
			throw convert(e);
		}

		
		listeners.fireUpdatedEvent(old, clone, new ArrayList<UpdateRequest>(updates));
	}
	



	private void applyUpdates(NodeBean node, Collection<UpdateRequest> updates) {
		for (UpdateRequest update : updates) {
			if(update instanceof UpdateName){
				String name = ((UpdateName) update).value;
				if(Check.isEmpty(name))
					continue;
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
					throw new IllegalArgumentException("unknow type for update url: "+node);
				}
			}
			else if(update instanceof UpdatePath){
				if(node instanceof FileLinkBean){
					String path = ((UpdatePath) update).value;
					((FileLinkBean)node).setPath(path);
				} else {
					throw new IllegalArgumentException("unknow type for update path: "+node);
				}
			}
			else if(update instanceof SetProperty){
				Pair<String, String> keyValue = ((SetProperty) update).keyValue;
				if(keyValue != null){
					String key = keyValue.getFirst();
					String value = keyValue.getSecond();
					if(key != null){
						node.setProperty(key, value);
					}
				}
			}
			else if(update instanceof RemoveProperty){
				String key = ((RemoveProperty) update).key;
				if(key != null){
					node.removeProperty(key);
				}
			}
		}
	}
	
	private void applyExternalUpdates(NodeBean node, Collection<UpdateRequest> updates) throws Exception {
		for (UpdateRequest update : updates) {
			if(update instanceof UpdateText){
				if(node instanceof TextBean){
					TextBean text = (TextBean)node;
					String content = ((UpdateText)update).value;
					if(!Check.isEmpty(content)){
						c.textService.setText(text, content);
					} else {
						c.textService.removeText(text);
					}
				} else {
					throw new IllegalArgumentException("unknow type for update text: "+node);
				}
			}
		}
	}


	private NodeBean convert(Node node) {
		return c.converter.convert(node);
	}

	private Exception convert(Throwable e) {
		if(e instanceof Exception) return (Exception)e;
		if(e instanceof Error) throw (Error) e;
		return new RuntimeException(e);
	}




}
