/**
 * 
 */
package ru.kc.tools.filepersist.persist;


import ru.kc.tools.filepersist.Tree;
import ru.kc.tools.filepersist.impl.InitContext;
import ru.kc.tools.filepersist.persist.model.ContainersModel;

public class FSContext {
	public final ContainersModel containerModel;
	public final ContainerStore containerStore;
	public final Tree persistService;
	public final InitContext init;
	
	public FSContext(ContainersModel containerModel,
			ContainerStore containerStore, Tree persistService, InitContext init) {
		super();
		this.containerModel = containerModel;
		this.containerStore = containerStore;
		this.persistService = persistService;
		this.init = init;
	}
	

	

	

}