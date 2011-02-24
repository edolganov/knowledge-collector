package ru.kc.tools.filepersist.persist.transaction;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UserTransaction {
	private static Log log = LogFactory.getLog(UserTransaction.class);
	private static final ThreadLocal<UserTransaction> THREAD_LOCAL = new ThreadLocal<UserTransaction>();
	
	static UserTransaction getExistOrCreateAndBegin(){
		UserTransaction userTransaction = THREAD_LOCAL.get();
		if(userTransaction == null){
			userTransaction = new UserTransaction(true);
			userTransaction.begin();
		}
		return userTransaction;
	}
	
	
	private boolean begined = false;
	private boolean singleTransaction;
	private Transaction<?> curTransaction;
	private ArrayList<Transaction<?>> done = new ArrayList<Transaction<?>>();
	
	public UserTransaction() {
		this(false);
	}

	private UserTransaction(boolean singleTransaction) {
		super();
		this.singleTransaction = singleTransaction;
	}


	public void begin(){
		if(THREAD_LOCAL.get() != null)
			throw new IllegalStateException("UserTransaction is already exist in current thread");
		THREAD_LOCAL.set(this);
		begined = true;
	}
	
	void add(Transaction<?> tr){
		if(!begined)
			throw new IllegalStateException("UserTransaction is not begined. call begin() before");
		curTransaction = tr;
	}
	
	void commitIfSingleTransaction() throws Throwable {
		moveCurTransactionToDone();
		if(singleTransaction){
			commit();
		}
	}

	public void commit() throws Throwable{
		try {
			tryCommit();
		} finally {
			clear();
		}
	}

	private void tryCommit() throws Throwable {
		for(Transaction<?> tr : done){
			tr.commit();
		}
		
		for(Transaction<?> tr : done){
			try {
				tr.fireTransactionCommitedForListeners();
			} catch (Throwable t) {
				log.error("process listener error",t);
			}
		}
	}
	


	public void rollback(){
		try {
			tryRollback();
		}finally {
			clear();
		}
	}

	private void tryRollback() {
		moveCurTransactionToDone();
		for (int i = done.size()-1; i > -1; i--) {
			Transaction<?> tr = done.remove(i);
			tr.roolback();
		}
	}
	
	private void moveCurTransactionToDone(){
		if(curTransaction != null){
			done.add(curTransaction);
			curTransaction = null;
		}
	}
	
	private void clear() {
		THREAD_LOCAL.set(null);
	}

	@Override
	public String toString() {
		return "UserTransaction [done=" + done + "]";
	}





}
