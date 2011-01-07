package ru.kc.tools.filepersist;

import java.io.File;

import ru.kc.tools.filepersist.command.Command;
import ru.kc.tools.filepersist.model.DataFactory;
import ru.kc.tools.filepersist.persist.PersistManager;

public class Context {
	
	public final File rootDir;
	public final PersistManager persistManager;
	public final DataFactory dataFactory;
	
	public Context(File rootDir, PersistManager persistManager,DataFactory dataFactory) {
		super();
		this.rootDir = rootDir;
		this.persistManager = persistManager;
		this.dataFactory = dataFactory;
	}

	public <O> O invoke(Command<O> command) throws Exception{
		command.setContext(this);
		O out = command.invoke();
		return out;
	}

}
