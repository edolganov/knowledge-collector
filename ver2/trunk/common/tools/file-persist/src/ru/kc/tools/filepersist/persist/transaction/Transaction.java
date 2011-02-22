package ru.kc.tools.filepersist.persist.transaction;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.tools.filepersist.persist.FSContext;

public abstract class Transaction<T> {
	
	private static Log log = LogFactory.getLog(Transaction.class);
	
	protected final FSContext c;
	private ArrayList<AtomicAction<?>> done = new ArrayList<AtomicAction<?>>();
	private ActionListeners actionListeners;

	public Transaction(FSContext c) {
		super();
		this.c = c;
		actionListeners = c.actionListeners;
	}
	
	protected abstract T body() throws Throwable;
	
	public T start() throws Exception {
		try{
			T out = body();
			transactionCommitedEvent();
			return out;
		} catch (Throwable e) {
			roolback();
			if(e instanceof Exception) throw (Exception)e;
			if(e instanceof Error) throw (Error)e;
			else throw new IllegalStateException(e);
		}
	}

	public <O> O invoke(AtomicAction<O> action) throws Throwable {
		action.init(this,c);
		O out = action.invoke();
		done.add(action);
		actionListeners.fireInvoke(action);
		return (O) out;
	}
	
	private void transactionCommitedEvent() {
		AtomicAction<?> action = null;
		for(AtomicAction<?> curAction : done){
			action = curAction;
			try{
				actionListeners.fireTransactionCommited(action);
			}catch (Throwable e) {
				log.error("can't commit transaction. [unrollbacked-actions="+done+", exception-in-action="+action+"]", e);
			}
		}
	}
	
	protected void roolback(){
		AtomicAction<?> action = null;
		try{
			for (int i = done.size()-1; i > -1; i--) {
				action = done.remove(i);
				action.rollback();
				actionListeners.fireRollback(action);
			}
		}catch (Throwable e) {
			log.error("can't rollback transaction. [unrollbacked-actions="+done+", exception-in-action="+action+"]", e);
		}
	}


	

	
}
