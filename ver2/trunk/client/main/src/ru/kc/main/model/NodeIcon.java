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
	private static final Icon fileLink = IconUtil.get("/ru/kc/main/img/fileLink.png");
	private static final Icon link = IconUtil.get("/ru/kc/main/img/link.png");
	private static final Icon text = IconUtil.get("/ru/kc/main/img/text.png");
	
	
	public static Icon getIcon(Node node){
		return getIcon(node.getClass());
	}
	
	public static Icon getIcon(Class<? extends Node> type){
		
		if(Dir.class.isAssignableFrom(type))
			return dir;
		if(Text.class.isAssignableFrom(type))
			return text;
		if(Link.class.isAssignableFrom(type))
			return link;
		if(FileLink.class.isAssignableFrom(type))
			return fileLink;
		
		return unknowType;
	}

}
