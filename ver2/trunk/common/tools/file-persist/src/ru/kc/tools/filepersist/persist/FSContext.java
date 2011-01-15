/**
 * 
 */
package ru.kc.tools.filepersist.persist;


import ru.kc.tools.filepersist.InitContext;
import ru.kc.tools.filepersist.PersistService;
import ru.kc.tools.filepersist.persist.model.ContainersModel;

public class FSContext {
	public final ContainersModel containerModel;
	public final ContainerStore containerStore;
	public final PersistService persistService;
	public final InitContext init;
	
	public FSContext(ContainersModel containerModel,
			ContainerStore containerStore, PersistService persistService, InitContext init) {
		super();
		this.containerModel = containerModel;
		this.containerStore = containerStore;
		this.persistService = persistService;
		this.init = init;
	}
	

	

	

}