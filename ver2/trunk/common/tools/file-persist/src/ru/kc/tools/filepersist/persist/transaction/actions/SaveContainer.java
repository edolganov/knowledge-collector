package ru.kc.tools.filepersist.persist.transaction.actions;

import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.persist.transaction.AtomicAction;

public class SaveContainer extends AtomicAction<Void>{
	
	Container container;

	public SaveContainer(Container container) {
		super();
		this.container = container;
	}

	@Override
	public Void invoke() throws Exception {
		c.containerStore.save(container);
		return null;
	}

	@Override
	public void rollback() throws Exception {
		c.containerStore.rollback(container);
	}

}
