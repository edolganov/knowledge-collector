package ru.kc.common;

import ru.kc.common.dialog.Dialogs;
import ru.kc.common.node.NodeConstants;
import ru.kc.common.node.edit.NodeEditionsAggregator;
import ru.kc.tools.filepersist.PersistService;

public class Context {
	
	public final PersistService persistService;
	public final NodeEditionsAggregator nodeEditionsAggregator;
	public final Dialogs dialogs;
	public final NodeConstants nodeConstants;
	
	public Context(PersistService persistService,
			NodeEditionsAggregator nodeEditionsAggregator, Dialogs dialogs) {
		super();
		this.persistService = persistService;
		this.nodeEditionsAggregator = nodeEditionsAggregator;
		this.dialogs = dialogs;
		this.nodeConstants = new NodeConstants();
	}
	




}
