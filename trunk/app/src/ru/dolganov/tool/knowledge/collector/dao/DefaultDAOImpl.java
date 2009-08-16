package ru.dolganov.tool.knowledge.collector.dao;

import java.io.File;

import ru.chapaj.util.store.XmlStore;
import ru.chapaj.util.xml.ObjectToXMLConverter;
import model.knowledge.Dir;
import model.knowledge.Image;
import model.knowledge.LocalLink;
import model.knowledge.NetworkLink;
import model.knowledge.Note;
import model.knowledge.Root;
import model.knowledge.TreeLink;
import model.tree.TreeSnapshot;
import model.tree.TreeSnapshotDir;
import model.tree.TreeSnapshotRoot;

public class DefaultDAOImpl implements DAO {
	
	XmlStore<Root> metaStore = new XmlStore<Root>(){

		@Override
		protected void config(ObjectToXMLConverter<Root> converter) {
			converter.configureAliases(
					Root.class,
					Dir.class,
					LocalLink.class,
					NetworkLink.class,
					Note.class,
					Image.class,
					TreeSnapshotDir.class,
					TreeSnapshotRoot.class,
					TreeSnapshot.class,
					TreeLink.class
					);
			
		}
		
	};

	File metaFile;
	Root metaRoot;
	
	
	
	
	public DefaultDAOImpl(String filePath) {
		metaFile = new File(filePath);
		if(!metaFile.exists()){
			metaRoot = new Root();
			Dir dir = new Dir();
			dir.setName("root");
			metaRoot.setRoot(dir);
		}
		else {
			try {
				metaRoot = metaStore.loadFile(metaFile);
			} catch (Exception e) {
				throw new IllegalArgumentException("can't load data from store",e);
			}
		}
		importLC();
	}




	private void importLC() {
		//ImportLinkCollectorData.fill("./import.xml", metaRoot);
		
	}




	@Override
	public Root getRoot() {
		return metaRoot;
	}




	@Override
	public void flushMeta() {
		try {
			metaStore.saveFile(metaFile, metaRoot, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
