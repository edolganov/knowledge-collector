/**
 * 
 */
package ru.kc.tools.filepersist.persist;

import java.io.File;

import ru.kc.tools.filepersist.PersistService;
import ru.kc.tools.filepersist.persist.model.ContainersModel;

public class FSContext {
	public final ContainersModel containerModel;
	public final ContainerStore containerStore;
	public final PersistService persistService;
	public final File nodesDir;
	public final File blobsDir;
	public final int maxNodesInContainer;
	public final int maxContainerFilesInFolder;
	public final int maxFoldersInLevel;
	
	public FSContext(ContainersModel containerModel,
			ContainerStore containerStore, PersistService persistService,
			File nodesDir, File blobsDir, int maxNodesInContainer,
			int maxContainerFilesInFolder,int maxFoldersInLevel) {
		super();
		this.containerModel = containerModel;
		this.containerStore = containerStore;
		this.persistService = persistService;
		this.nodesDir = nodesDir;
		this.blobsDir = blobsDir;
		this.maxNodesInContainer = maxNodesInContainer;
		this.maxContainerFilesInFolder = maxContainerFilesInFolder;
		this.maxFoldersInLevel = maxFoldersInLevel;
	}
	

	

}