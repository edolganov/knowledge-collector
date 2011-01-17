package ru.kc.tools.filepersist.impl;

import ru.kc.model.Dir;
import ru.kc.model.FileLink;
import ru.kc.model.Link;
import ru.kc.model.Text;
import ru.kc.tools.filepersist.Factory;
import ru.kc.tools.filepersist.model.impl.DirBean;
import ru.kc.tools.filepersist.model.impl.FileLinkBean;
import ru.kc.tools.filepersist.model.impl.LinkBean;
import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.model.impl.TextBean;
import ru.kc.util.UuidGenerator;

public class FactoryImpl implements Factory {
	
	public void init(Context context){
		
	}


	@Override
	public Dir createDir(String name, String description) {
		DirBean dir = new DirBean();
		init(dir,name,description);
		return dir;
	}

	@Override
	public FileLink createFileLink(String name, String path, String description) {
		FileLinkBean link = new FileLinkBean();
		init(link, name, description);
		link.setPath(path);
		return link;
	}

	@Override
	public Link createLink(String name, String url, String description) {
		LinkBean link = new LinkBean();
		init(link, name, description);
		link.setUrl(url);
		return link;
	}

	@Override
	public Text createText(String name, String txt) {
		TextBean text = new TextBean();
		init(text, name, null);
		text.setTextToSave(txt);
		return text;
	}
	
	
	private void init(NodeBean node, String name, String description) {
		node.setId(generateId());
		node.setCreateDate(System.currentTimeMillis());
		node.setName(name);
		node.setDescription(description);
	}

	private String generateId() {
		return UuidGenerator.simpleUuid(3);
	}






}
