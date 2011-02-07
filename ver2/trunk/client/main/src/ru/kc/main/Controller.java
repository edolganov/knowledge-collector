package ru.kc.main;

import ru.kc.platform.annotations.Inject;
import ru.kc.platform.controller.AbstractController;

public abstract class Controller<T> extends AbstractController<T>{
	
	@Inject
	protected Context context;

}
