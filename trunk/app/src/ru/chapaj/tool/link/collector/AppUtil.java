package ru.chapaj.tool.link.collector;

import ru.chapaj.tool.link.collector.model.Dir;
import ru.chapaj.tool.link.collector.model.Link;

public class AppUtil {

	public static boolean isDir(Object obj){
		return obj instanceof Dir;
	}
	
	public static boolean isLink(Object obj){
		return obj instanceof Link;
	}
}
