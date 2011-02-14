package ru.kc.tools.filepersist.impl;

import java.io.File;

public class InitParams {
	
	public final File rootDir;
	public final int maxNodesInContainer;
	public final int maxContainerFilesInFolder;
	public final int maxFoldersInLevel;
	
	
	public InitParams(File rootDir, int maxNodesInContainer,
			int maxContainerFilesInFolder, int maxFoldersInLevel) {
		super();
		this.rootDir = rootDir;
		this.maxNodesInContainer = maxNodesInContainer;
		this.maxContainerFilesInFolder = maxContainerFilesInFolder;
		this.maxFoldersInLevel = maxFoldersInLevel;
	}
	

	
	

}
