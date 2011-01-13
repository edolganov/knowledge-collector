package ru.kc.tools.filepersist.persist.transaction.actions;

import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.persist.transaction.AtomicAction;

public class GetNotFullContainer extends AtomicAction<Container>{

	@Override
	protected Container invoke() throws Exception {
		return c.containerModel.getNotFullContainer();
	}

	@Override
	protected void rollback() {}

}
