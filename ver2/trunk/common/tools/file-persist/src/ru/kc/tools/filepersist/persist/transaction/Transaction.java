package ru.kc.tools.filepersist.persist.transaction;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.tools.filepersist.persist.ContainerStore;
import ru.kc.tools.filepersist.persist.model.ContainerNameModel;
import ru.kc.tools.filepersist.persist.model.ContainersModel;

public class Transaction {
	
	private static Log log = LogFactory.getLog(Transaction.class);

	public final ContainersModel containerModel;
	public final ContainerNameModel containerNameModel;
	public final ContainerStore containerStore;
	
	ArrayList<AtomicAction<?>> done = new ArrayList<AtomicAction<?>>();
	

	public Transaction(ContainersModel containerModel,
			ContainerNameModel containerNameModel,
			ContainerStore containerStore) {
		super();
		this.containerModel = containerModel;
		this.containerNameModel = containerNameModel;
		this.containerStore = containerStore;
	}
	
	public <O> O invoke(AtomicAction<O> action) throws Exception {
		try {
			action.setTransaction(this);
			O out = action.invoke();
			done.add(action);
			return (O) out;
		}catch (Throwable e) {
			if(e instanceof Exception) throw (Exception)e;
			if(e instanceof Error) throw (Error)e;
			else throw new IllegalStateException(e);
 		}
		
	}
	
	public void roolback(){
		AtomicAction<?> action = null;
		try{
			for (int i = done.size()-1; i > -1; i--) {
				action = done.remove(i);
				action.rollback();
			}
		}catch (Throwable e) {
			log.error("can't rollback transaction. [unrollbacked-actions="+done+", exception-in-action="+action+"]", e);
		}
	}
	
}
