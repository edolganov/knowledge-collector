package ru.kc.tools.filepersist.persist;


import ru.kc.tools.filepersist.impl.Context;
import ru.kc.tools.filepersist.persist.model.ContainersModel;
import ru.kc.tools.filepersist.persist.transaction.ActionListeners;

public class FSContext {
	
	public final ContainersModel containerModel;
	public final ContainerStore containerStore;
	public final Context c;
	public final Blobs blobs;
	public final ActionListeners actionListeners;
	
	
	public FSContext(ContainersModel containerModel,
			ContainerStore containerStore, Context c, Blobs blobs) {
		super();
		this.containerModel = containerModel;
		this.containerStore = containerStore;
		this.c = c;
		this.blobs = blobs;
		this.actionListeners = new ActionListeners();
	}
	
	
	
	
	
	
	
	

	

	

}