package filepersist;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.kc.tools.filepersist.InitParams;
import ru.kc.tools.filepersist.impl.Context;
import ru.kc.tools.filepersist.impl.InitContextExt;
import ru.kc.tools.filepersist.persist.ContainerStore;
import ru.kc.tools.filepersist.persist.FSContext;
import ru.kc.tools.filepersist.persist.transaction.Transaction;
import ru.kc.tools.filepersist.persist.transaction.TransactionsJournal;
import ru.kc.util.file.FileUtil;
import junit.framework.Assert;

public class TransactionsJournalTest extends Assert {
	
	File dir = new File("./test_data");
	FSContext context;
	TransactionsJournal journal;
	
	@Before
	public void init() throws Exception{
		InitParams params = new InitParams(dir, 2, 2, 2);
		InitContextExt init = new InitContextExt(params, dir, null);
		
		Context c = new Context(init, null, null, null);
		journal = new TransactionsJournal();
		ContainerStore store = new ContainerStore();
		context = new FSContext(null, store, c,journal);
		store.init(context);
		journal.init(context);
	}
	
	@After
	public void deleteDir() {
		FileUtil.deleteDirRecursive(dir);
	}
	
	@Test
	public void transactionDirCreated(){
		File journalDir = journal.getJournalDir();
		assertEquals(true, journalDir.exists());
		assertEquals(true, journalDir.isDirectory());
	}
	
	@Test(expected=IllegalStateException.class)
	public void cantStartToTransactionInOneRecord(){
		try {
			journal.start(new TransactionStub(context));
			journal.start(new TransactionStub(context));
		}finally{
			journal.cancelRecord();
		}
	}
	
	@Test
	public void cantWorkWithoutStart(){
		try{
			journal.invoked(new TransactionStub(context), null);
			fail("IllegalStateException expected");
		}catch (IllegalStateException e) {
			//good
		}
		
		try{
			journal.finish(new TransactionStub(context));
			fail("IllegalStateException expected");
		}catch (IllegalStateException e) {
			//good
		}
		
		try{
			journal.cancelRecord();
			fail("IllegalStateException expected");
		}catch (IllegalStateException e) {
			//good
		}
	}
	
	public void addNodeTest(){
		
	}

}


class TransactionStub extends Transaction<Void> {

	public TransactionStub(FSContext c) {
		super(c);
	}

	@Override
	protected Void body() throws Throwable {
		return null;
	}
	
}
