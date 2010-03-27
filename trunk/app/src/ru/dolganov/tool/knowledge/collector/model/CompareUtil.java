package ru.dolganov.tool.knowledge.collector.model;

import ru.chapaj.util.lang.ClassUtil;
import model.knowledge.Dir;
import model.knowledge.LocalLink;
import model.knowledge.NetworkLink;
import model.knowledge.RootElement;
import model.knowledge.TextData;

public class CompareUtil {
	
	public static int index(RootElement meta){
		return index(meta.getClass());
	}
	
	public static int index(Class<? extends RootElement> candidat){
		int out = Integer.MAX_VALUE;
		if(ClassUtil.isValid(candidat, Dir.class)) out = 0;
		else if(ClassUtil.isValid(candidat, TextData.class)) out = 10;
		else if(ClassUtil.isValid(candidat,LocalLink.class)) out = 20;
		else if(ClassUtil.isValid(candidat,NetworkLink.class)) out = 30;
		return out;
	}
	
	public static int compare(int na, int nb){
		return (na<nb ? -1 : (na==nb ? 0 : 1));
	}

}
