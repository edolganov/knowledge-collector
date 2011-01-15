package ru.kc.tools.filepersist.impl;

import ru.kc.tools.filepersist.command.Command;
import ru.kc.tools.filepersist.persist.FileSystemImpl;

public class Context {
	
	public final FileSystemImpl fs;
	public final FactoryImpl dataFactory;
	public final TreeImpl persistService;
	public final ConvertorServiceImpl convertorService;
	

	public Context(FileSystemImpl fs, FactoryImpl dataFactory,
			TreeImpl persistService,
			ConvertorServiceImpl convertorService) {
		super();
		this.fs = fs;
		this.dataFactory = dataFactory;
		this.persistService = persistService;
		this.convertorService = convertorService;
	}


	public <O> O invoke(Command<O> command) throws Exception{
		command.setContext(this);
		O out = command.invoke();
		return out;
	}

}
