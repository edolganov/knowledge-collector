package ru.chapaj.tool.link.collector.store.file;

import ru.chapaj.tool.link.collector.model.DataContainer;
import ru.chapaj.tool.link.collector.model.Dir;
import ru.chapaj.tool.link.collector.model.Link;
import ru.chapaj.tool.link.collector.model.TreeSnapshot;
import ru.chapaj.tool.link.collector.model.TreeSnapshotDir;
import ru.chapaj.util.store.XmlStore;
import ru.chapaj.util.xml.ObjectToXMLConverter;

public class DataContainerStore extends XmlStore<DataContainer> {

	public DataContainerStore() {
		super(DataContainer.class);
	}

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
