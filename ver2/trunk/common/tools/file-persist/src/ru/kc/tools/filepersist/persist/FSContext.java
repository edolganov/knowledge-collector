package ru.kc.tools.filepersist.persist;


import ru.kc.tools.filepersist.impl.Context;
import ru.kc.tools.filepersist.persist.model.ContainersModel;
import ru.kc.tools.filepersist.persist.transaction.TransactionsJournal;

public class FSContext {
	public final ContainersModel containerModel;
	public final ContainerStore containerStore;
	public final Context c;
	public final TransactionsJournal journal;
	
	public FSContext(ContainersModel containerModel,
			ContainerStore containerStore, Context c, TransactionsJournal journal) {
		super();
		this.containerModel = containerModel;
		this.containerStore = containerStore;
		this.c = c;
		this.journal = journal;
	}
	
	
	

	

	

}