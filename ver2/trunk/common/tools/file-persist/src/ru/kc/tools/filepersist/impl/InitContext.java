package ru.kc.tools.filepersist.impl;

import java.io.File;

public class InitContext {
	
	public final File nodesDir;
	public final File blobsDir;
	public final int maxNodesInContainer;
	public final int maxContainerFilesInFolder;
	public final int maxFoldersInLevel;
	
	
	public InitContext(int maxNodesInContainer,
			int maxContainerFilesInFolder, int maxFoldersInLevel) {
		super();
		this.nodesDir = null;
		this.blobsDir = null;
		this.maxNodesInContainer = maxNodesInContainer;
		this.maxContainerFilesInFolder = maxContainerFilesInFolder;
		this.maxFoldersInLevel = maxFoldersInLevel;
	}
	
	public InitContext(File nodesDir, File blobsDir, int maxNodesInContainer,
			int maxContainerFilesInFolder, int maxFoldersInLevel) {
		super();
		this.nodesDir = nodesDir;
		this.blobsDir = blobsDir;
		this.maxNodesInContainer = maxNodesInContainer;
		this.maxContainerFilesInFolder = maxContainerFilesInFolder;
		this.maxFoldersInLevel = maxFoldersInLevel;
	}

	

	
	

}
