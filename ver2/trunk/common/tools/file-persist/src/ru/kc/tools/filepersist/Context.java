package ru.kc.tools.filepersist;

import java.io.File;

import ru.kc.tools.filepersist.command.Command;
import ru.kc.tools.filepersist.model.DataFactoryImpl;
import ru.kc.tools.filepersist.persist.FileSystemImpl;

public class Context {
	
	public final File rootDir;
	public final FileSystemImpl fs;
	public final DataFactoryImpl dataFactory;
	public final PersistServiceImpl persistService;
	
	public Context(File rootDir, FileSystemImpl fs,DataFactoryImpl dataFactory,PersistServiceImpl persistService) {
		super();
		this.rootDir = rootDir;
		this.fs = fs;
		this.dataFactory = dataFactory;
		this.persistService = persistService;
	}

	public <O> O invoke(Command<O> command) throws Exception{
		command.setContext(this);
		O out = command.invoke();
		return out;
	}

}
