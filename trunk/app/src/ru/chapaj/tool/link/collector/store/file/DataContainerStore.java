package ru.chapaj.tool.link.collector.store.file;

import model.tree.TreeSnapshot;
import model.tree.TreeSnapshotDir;
import ru.chapaj.tool.link.collector.model.DataContainer;
import ru.chapaj.tool.link.collector.model.Dir;
import ru.chapaj.tool.link.collector.model.Link;
import ru.chapaj.util.store.XmlStore;
import ru.chapaj.util.xml.ObjectToXMLConverter;

public class DataContainerStore extends XmlStore<DataContainer> {

	@Override
	protected void config(ObjectToXMLConverter<DataContainer> converter) {
		converter.configureAliases(
				DataContainer.class,
				Dir.class,
				Link.class,
				TreeSnapshot.class,
				TreeSnapshotDir.class
				);
	}

}
