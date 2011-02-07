package ru.kc.main.model;

import javax.swing.Icon;

import ru.kc.model.Dir;
import ru.kc.model.FileLink;
import ru.kc.model.Link;
import ru.kc.model.Node;
import ru.kc.model.Text;
import ru.kc.util.swing.icon.IconUtil;

public class NodeIcon {
	
	private static final Icon unknowType = IconUtil.get("/ru/kc/main/img/node.png");
	private static final Icon dir = IconUtil.get("/ru/kc/main/img/dir.png");
	private static final Icon localLink = IconUtil.get("/ru/kc/main/img/localLink.png");
	private static final Icon netLink = IconUtil.get("/ru/kc/main/img/netLink.png");
	private static final Icon note = IconUtil.get("/ru/kc/main/img/note.png");
	
	
	public static Icon getIcon(Node node){
		
		if(node instanceof Dir)
			return dir;
		if(node instanceof Text)
			return note;
		if(node instanceof Link)
			return netLink;
		if(node instanceof FileLink)
			return localLink;
		
		return unknowType;
	}

}
