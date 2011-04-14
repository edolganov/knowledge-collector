package ru.kc.tools.filepersist.persist;


import ru.kc.tools.filepersist.impl.Context;
import ru.kc.tools.filepersist.persist.model.ContainersModel;
import ru.kc.tools.filepersist.persist.transaction.ActionListeners;
import ru.kc.tools.filepersist.persist.transaction.InterceptorsManager;

public class FSContext {
	
	public final ContainersModel containerModel;
	public final ContainerStore containerStore;
	public final Context c;
	public final Blobs blobs;
	public final ActionListeners actionListeners;
	public final InterceptorsManager interceptorsManager;
	
	
	public FSContext(ContainersModel containerModel,
			ContainerStore containerStore, Context c, Blobs blobs, InterceptorsManager interceptorsManager) {
		super();
		this.containerModel = containerModel;
		this.containerStore = containerStore;
		this.c = c;
		this.blobs = blobs;
		this.actionListeners = new ActionListeners();
		this.interceptorsManager = interceptorsManager;
	}
	
	
	
	
	
	
	
	

	

	

}