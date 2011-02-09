package ru.kc.tools.filepersist.persist.transaction;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.persist.ContainerListener;
import ru.kc.tools.filepersist.persist.ContainerStore;
import ru.kc.tools.filepersist.persist.FSContext;
import ru.kc.tools.filepersist.persist.transaction.model.JournalRecord;
import ru.kc.util.xml.ObjectToXMLConverter;
import ru.kc.util.xml.XmlStore;

public class TransactionsJournal {
	
	private static Log log = LogFactory.getLog(TransactionsJournal.class);
	
	private static final ThreadLocal<JournalRecord> threadLocalRecord = new ThreadLocal<JournalRecord>();
	
	private File rootDir;
	private File journalDir;
	private File sessionDir;
	private String sessionId;
	private int recordNumber;
	private XmlStore<JournalRecord> xmlStore;

	public void init(FSContext c) {
		rootDir = c.c.init.params.rootDir;
		journalDir = new File(rootDir,"tr-journal");
		journalDir.mkdirs();
		
		sessionId = "session-"+System.currentTimeMillis();
		sessionDir = new File(rootDir,sessionId);
		if(sessionDir.exists())
			throw new IllegalStateException("session dir "+sessionDir+" already exist");
		sessionDir.mkdir();
		
		recordNumber = 0;
		
		xmlStore = new XmlStore<JournalRecord>() {
			
			@Override
			protected void config(ObjectToXMLConverter<JournalRecord> converter) {
				converter.configureAliases(JournalRecord.class);
			}
		};
		
		ContainerStore store = c.containerStore;
		store.addListener(new ContainerListener() {
			
			@Override
			public void onSaved(Container container) {
				if(hasRecordInThreadLocal()){
					File file = container.getFile();
					saved(file);
				}
			}
		});
	}

	public File getJournalDir(){
		return journalDir;
	}
	
	
	public void start(Transaction<?> transaction){
		createNewRecordInThreadLocal();
	}

	public void invoked(Transaction<?> transaction, AtomicAction<?> action){
		JournalRecord record = getCurrentRecordFromThreadLocal();
		record.addEvent("invoked:"+action.getClass().getName());
	}
	
	protected void saved(File file) {
		JournalRecord record = getCurrentRecordFromThreadLocal();
		record.addEvent("saved:"+getPath(file));
		
	}

	
	private String getPath(File file) {
		String path = file.getAbsolutePath();
		String rootPath = rootDir.getAbsolutePath();
		String out = path.substring(rootPath.length());
		return out;
	}

	public void finish(Transaction<?> transaction){
		JournalRecord record = removeCurrentRecordFromThreadLocal();
		saveSuccessRecord(record);
	}

	public void rollback(Transaction<?> transaction){
		JournalRecord record = removeCurrentRecordFromThreadLocal();
		saveRollbackRecord(record);
	}

	public void cancelRecord() {
		removeCurrentRecordFromThreadLocal();
	}

	
	private JournalRecord createNewRecordInThreadLocal() {
		JournalRecord old = threadLocalRecord.get();
		if(old != null) 
			throw new IllegalStateException("current thread already contains "+old);
		
		JournalRecord newRecord = createNewRecord();
		return newRecord;
	}
	
	private JournalRecord removeCurrentRecordFromThreadLocal() {
		JournalRecord record = getCurrentRecordFromThreadLocal();
		deleteRecordFromThreadLocal();
		return record;
	}
	
	private boolean hasRecordInThreadLocal(){
		return threadLocalRecord.get() != null;
	}
	
	private JournalRecord getCurrentRecordFromThreadLocal() {
		JournalRecord cur = threadLocalRecord.get();
		if(cur == null) 
			throw new IllegalStateException("current thread don't contains record");
		return cur;
	}
	
	private void deleteRecordFromThreadLocal(){
		threadLocalRecord.set(null);
	}

	
	private synchronized JournalRecord createNewRecord() {
		JournalRecord newRecord = new JournalRecord();
		newRecord.setCreateDate(System.currentTimeMillis());
		newRecord.setSessionId(sessionId);
		newRecord.setNumberInSession(++recordNumber);
		threadLocalRecord.set(newRecord);
		return newRecord;
	}
	
	private synchronized void saveSuccessRecord(JournalRecord record) {
		try{
			record.setFinished(true);
			save(record);
		}catch (Exception e) {
			log.error("error while save success record",e);
		}
		
	}
	
	private synchronized void saveRollbackRecord(JournalRecord record) {
		try{
			record.setFinished(false);
			save(record);
		}catch (Exception e) {
			log.error("error while save rollback record",e);
		}
	}
	
	private void save(JournalRecord record) throws Exception {
		String fileName = "tr-"+record.getNumberInSession();
		File file = new File(sessionDir,fileName);
		xmlStore.saveFile(file, record, false);
	}




}
