package ru.kc.tools.filepersist.command;

import ru.kc.tools.filepersist.impl.Context;

public abstract class Command<O> {
	
	protected Context c;

	public abstract O invoke() throws Exception;

	protected <D> D invoke(Command<D> command) throws Exception{
		return c.invoke(command);
	}
	
	public void setContext(Context context) {
		this.c = context;
	}	
	
	

}
