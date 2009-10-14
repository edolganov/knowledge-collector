package ru.dolganov.tool.knowledge.collector.dao.fs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ru.chapaj.tool.link.collector.model.DataContainer;
import ru.chapaj.tool.link.collector.model.Link;
import ru.chapaj.tool.link.collector.store.file.DataContainerStore;
import ru.dolganov.tool.knowledge.collector.AppUtil;
import ru.dolganov.tool.knowledge.collector.dao.DAO;
import ru.dolganov.tool.knowledge.collector.model.HasNodeMetaParams;
import ru.dolganov.tool.knowledge.collector.model.LinkOps;
import model.knowledge.Dir;
import model.knowledge.LocalLink;
import model.knowledge.NetworkLink;
import model.knowledge.NodeMeta;
import model.knowledge.Note;
import model.knowledge.TextData;
import model.knowledge.role.Parent;
import model.tree.TreeSnapshot;
import model.tree.TreeSnapshotDir;

public class ImportLinkCollectorData implements HasNodeMetaParams{
	
	static class QS {
		Parent parent;
		ru.chapaj.tool.link.collector.model.Dir lcDir;
		public QS(Parent parent, ru.chapaj.tool.link.collector.model.Dir lcDir) {
			super();
			this.parent = parent;
			this.lcDir = lcDir;
		}
	}
	
	public static void fill(String lcFilePath, DAO dao){
		try {
			DataContainer dc = new DataContainerStore().loadFile(new File(lcFilePath));
			
			//tree
			LinkedList<QS> q = new LinkedList<QS>();
			q.add(new QS(dao.getRoot(),dc.getRoot()));
			while(!q.isEmpty()){
				QS qs = q.removeFirst();
				ArrayList<ru.chapaj.tool.link.collector.model.Dir> dirs = qs.lcDir.getSubDir();
				if(dirs != null){
					for(ru.chapaj.tool.link.collector.model.Dir lcDir : dirs){
						Dir dir = convertDir(lcDir);
						dao.addChild(qs.parent, dir);
						q.addLast(new QS(dir,lcDir));
						
					}
				}
				
				ArrayList<Link> links = qs.lcDir.getLinks();
				if(links != null){
					for(Link link : links){
						NodeMeta node = convertLink(link);
						HashMap<String, String> params = null;
						if(node instanceof TextData){
							params = new HashMap<String, String>(1);
							params.put(Params.text.toString(), link.getDescription());
						}
						dao.addChild(qs.parent, node,params);
					}
				}
			}
			
			
			//snapshots
			TreeSnapshot lastTreeState = dc.getLastTreeState();
			if(lastTreeState != null) {
				dao.add(lastTreeState,AppUtil.map("lastTreeState", null));
			}
			List<TreeSnapshot> snapshots = dc.getSnapshots();
			for(TreeSnapshot ts : snapshots) 
					dao.add(ts, AppUtil.map("snapshot","main"));
			List<TreeSnapshotDir> snaphotDirs = dc.getSnaphotDirs();
			for(TreeSnapshotDir dir : snaphotDirs){
				for(TreeSnapshot ts : dir.getSnapshots())
					dao.add(ts, AppUtil.map("snapshot", dir.getName()));
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static NodeMeta convertLink(Link ob) {
		NodeMeta meta = null;
		String url = ob.getUrl();
		if(LinkOps.isInetLink(url)){
			meta = new NetworkLink();
			((model.knowledge.Link)meta).setUrl(ob.getUrl());
			meta.setDescription(ob.getDescription());
		}
		else if(LinkOps.isLocalLink(url)){
			meta = new LocalLink();
			((model.knowledge.Link)meta).setUrl(ob.getUrl());
			meta.setDescription(ob.getDescription());
		}
		else meta = new Note();
		
		meta.setCreateDate(System.currentTimeMillis());
		meta.setName(ob.getName());
		meta.setUuid(ob.getUuid());
		
		return meta;
	}

	private static Dir convertDir(ru.chapaj.tool.link.collector.model.Dir ob) {
		Dir dir = new Dir();
		dir.setCreateDate(System.currentTimeMillis());
		dir.setUuid(ob.getUuid());
		dir.setDescription(ob.getMeta().getDescription());
		dir.setName(ob.getMeta().getName());
//		dir.setTags(ob.getMeta())
		return dir;
	}

}
