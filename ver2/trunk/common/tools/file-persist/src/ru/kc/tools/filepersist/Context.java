package ru.kc.tools.filepersist;

import java.io.File;

import ru.kc.tools.filepersist.command.Command;
import ru.kc.tools.filepersist.model.DataFactory;
import ru.kc.tools.filepersist.persist.EntityManager;

public class Context {
	
	public final File rootDir;
	public final EntityManager entityManager;
	public final DataFactory dataFactory;
	
	public Context(File rootDir, EntityManager entityManager,DataFactory dataFactory) {
		super();
		this.rootDir = rootDir;
		this.entityManager = entityManager;
		this.dataFactory = dataFactory;
	}

	public <O> O invoke(Command<O> command) throws Exception{
		command.setContext(this);
		O out = command.invoke();
		return out;
	}

}
