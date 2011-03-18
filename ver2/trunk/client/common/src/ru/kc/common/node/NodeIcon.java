package ru.kc.common.node;

import javax.swing.Icon;

import ru.kc.model.Dir;
import ru.kc.model.FileLink;
import ru.kc.model.Link;
import ru.kc.model.Node;
import ru.kc.model.Text;
import ru.kc.util.swing.icon.IconUtil;

public class NodeIcon {
	
	
	
	public static final String UNKNOW_TYPE = "/ru/kc/common/img/node.png";
	public static final String DIR = "/ru/kc/common/img/dir.png";
	public static final String FILE_LINK = "/ru/kc/common/img/fileLink.png";
	public static final String LINK = "/ru/kc/common/img/link.png";
	public static final String TEXT = "/ru/kc/common/img/text.png";
	public static final String TEXT_FULL = "/ru/kc/common/img/text-full.png";
	
	
	private static final Icon unknowType = IconUtil.get(UNKNOW_TYPE);
	private static final Icon dir = IconUtil.get(DIR);
	private static final Icon fileLink = IconUtil.get(FILE_LINK);
	private static final Icon link = IconUtil.get(LINK);
	private static final Icon text = IconUtil.get(TEXT);
	private static final Icon textFull = IconUtil.get(TEXT_FULL);
	
	
	public static Icon getIcon(Node node){
		if(node instanceof Text){
			Text text = (Text)node;
			if(text.hasText()) return textFull;
		}
		return getIcon(node.getClass());
	}
	
	public static Icon getIcon(Class<?> type){
		
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
