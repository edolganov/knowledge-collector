package ru.kc.tools.filepersist.persist.transaction;

import java.io.File;

import ru.kc.tools.filepersist.persist.FSContext;
import ru.kc.tools.filepersist.persist.transaction.model.JournalRecord;

public class TransactionsJournal {
	
	private final static ThreadLocal<JournalRecord> threadLocalRecord = new ThreadLocal<JournalRecord>();
	private File journalDir;

	public void init(FSContext c) {
		File rootDir = c.c.init.params.rootDir;
		journalDir = new File(rootDir,"tr-journal");
		journalDir.mkdirs();
	}
	
	
	public void start(Transaction<?> transaction){
		JournalRecord record = createNewRecord();
	}



	public void invoked(Transaction<?> transaction, AtomicAction<?> action){
		JournalRecord record = getCurrentRecord();

	}


	public void finish(Transaction<?> transaction){
		JournalRecord record = getCurrentRecord();
		deleteRecordFromTreadLocal();
	}
	
	public void rollback(Transaction<?> transaction){
		JournalRecord record = getCurrentRecord();
		deleteRecordFromTreadLocal();
	}
	
	public void cancelRecord() {
		getCurrentRecord();
		deleteRecordFromTreadLocal();
	}
	
	public File getJournalDir(){
		return journalDir;
	}
	
	
	private JournalRecord createNewRecord() {
		JournalRecord old = threadLocalRecord.get();
		if(old != null) 
			throw new IllegalStateException("current thread already contains "+old);
		
		JournalRecord newRecord = new JournalRecord();
		threadLocalRecord.set(newRecord);
		return newRecord;
	}
	
	private JournalRecord getCurrentRecord() {
		JournalRecord cur = threadLocalRecord.get();
		if(cur == null) 
			throw new IllegalStateException("current thread don't contains record");
		return cur;
	}
	
	private void deleteRecordFromTreadLocal(){
		threadLocalRecord.set(null);
	}




}
