package ru.chapaj.tool.link.collector.model;

import java.util.ArrayList;

import ru.chapaj.util.collection.ListUtil;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("dir")
public class Dir implements HavingUuid {
	
	private ArrayList<Dir> subDir;
	
	private ArrayList<Link> links;
	
	@XStreamAlias("meta")
	private NodeMeta meta;
	
	
	public Dir() {
	}
	
	public Dir(String name){
		NodeMeta meta = new NodeMeta();
		meta.setName(name);
		this.setMeta(meta);
	}
	
	public NodeMeta getMeta() {
		if(meta == null) meta = new NodeMeta();
		return meta;
	}

	public void setMeta(NodeMeta meta) {
		this.meta = meta;
	}
	
	public ArrayList<Dir> getSubDir() {
		return subDir;
	}
	public void setSubDir(ArrayList<Dir> subDir) {
		this.subDir = subDir;
	}
	public ArrayList<Link> getLinks() {
		return links;
	}
	public void setLinks(ArrayList<Link> links) {
		this.links = links;
	}
	
	@Override
	public String toString() {
		return getMeta().toString();
	}

	public void addChild(Dir dir) {
		if(subDir == null) subDir = new ArrayList<Dir>();
		subDir.add(dir);
	}

	public void addChild(Link link) {
		if(links == null) links = new ArrayList<Link>();
		links.add(link);
		
	}

	public void remove(Object object) {
		if (object instanceof Dir) {
			Dir dir = (Dir) object;
			if(subDir != null) subDir.remove(dir);
		}
		else if (object instanceof Link) {
			Link link = (Link) object;
			if(links != null) links.remove(link);
		}
	}

	public void addChild(Object object) {
		if (object instanceof Dir) {
			addChild((Dir) object);
		}
		else if (object instanceof Link) {
			addChild((Link) object);
		}
		
	}

	public void moveDown(Object object) {
		if (object instanceof Dir) {
			ListUtil.moveDown(subDir, (Dir) object);
		}
		else if (object instanceof Link) {
			ListUtil.moveDown(links, (Link) object);
		}
	}
	
	public void moveUp(Object object) {
		if (object instanceof Dir) {
			ListUtil.moveUp(subDir, (Dir) object);
		}
		else if (object instanceof Link) {
			ListUtil.moveUp(links, (Link) object);
		}
	}

	@Override
	public String getUuid() {
		return getMeta().getUuid();
	}

}
