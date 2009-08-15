package ru.dolganov.tool.knowledge.collector.dao;

import ru.chapaj.util.store.XmlStore;
import ru.chapaj.util.xml.ObjectToXMLConverter;
import model.knowledge.Dir;
import model.knowledge.Image;
import model.knowledge.LocalLink;
import model.knowledge.NetworkLink;
import model.knowledge.Note;
import model.knowledge.Root;
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
					TreeSnapshot.class
					);
			
		}
		
	};

	@Override
	public Root getRoot() {
		return null;
	}

}
