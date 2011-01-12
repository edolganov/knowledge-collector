package ru.kc.tools.filepersist.persist.transaction.actions;

import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.persist.transaction.AtomicAction;

public class GetNotFullContainer extends AtomicAction<Container>{

	@Override
	protected Container invoke() throws Exception {
		Container out = t.containerModel.getNotFullContainer();
		if(out == null){
			//TODO
		}
		return out;
	}

	@Override
	protected void rollback() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
