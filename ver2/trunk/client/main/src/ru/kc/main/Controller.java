package ru.kc.main;

import ru.kc.platform.annotations.Inject;
import ru.kc.platform.controller.AbstractController;
import ru.kc.tools.filepersist.PersistService;
import ru.kc.tools.filepersist.Tree;

public abstract class Controller<T> extends AbstractController<T>{
	
	@Inject
	protected Context context;
	protected PersistService persist;
	protected Tree persistTree;
	
	@Override
	protected void beforeInit() {
		persist = context.persistService;
		persistTree = persist.tree();
	}
	
	

}
