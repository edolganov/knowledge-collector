package ru.kc.main;

import ru.kc.tools.filepersist.PersistService;

public class Context {
	
	public final PersistService persistService;

	public Context(PersistService persistService) {
		super();
		this.persistService = persistService;
	}

}
