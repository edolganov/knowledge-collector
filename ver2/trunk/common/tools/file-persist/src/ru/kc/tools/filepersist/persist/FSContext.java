package ru.kc.tools.filepersist.persist;


import ru.kc.tools.filepersist.impl.Context;
import ru.kc.tools.filepersist.persist.model.ContainersModel;

public class FSContext {
	public final ContainersModel containerModel;
	public final ContainerStore containerStore;
	public final Context c;
	
	public FSContext(ContainersModel containerModel,
			ContainerStore containerStore, Context c) {
		super();
		this.containerModel = containerModel;
		this.containerStore = containerStore;
		this.c = c;
	}
	
	
	

	

	

}