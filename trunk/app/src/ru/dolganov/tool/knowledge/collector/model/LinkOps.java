package ru.dolganov.tool.knowledge.collector.model;

public class LinkOps {
	
	public static boolean isInetLink(String url){
		if(url.startsWith("http")) return true;
		if(url.startsWith("www")) return true;
		return false;
	}
	
	public static boolean isLocalLink(String url){
		if(url.indexOf(':')==1) return true;
		if(url.indexOf('/')==0) return true;
		return false;
	}

}
