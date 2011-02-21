package filepersist;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ru.kc.tools.filepersist.impl.Context;
import ru.kc.tools.filepersist.impl.InitContextExt;
import ru.kc.tools.filepersist.impl.InitParams;
import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.persist.ContainerStore;
import ru.kc.tools.filepersist.persist.FSContext;
import ru.kc.tools.filepersist.persist.transaction.Transaction;
import ru.kc.tools.filepersist.persist.transaction.TransactionsJournal;
import ru.kc.tools.filepersist.persist.transaction.actions.SaveContainer;
import ru.kc.util.file.FileUtil;
import junit.framework.Assert;

@Ignore
public class TransactionsJournalTest extends Assert {
	
	File dir = new File("./test_data");
	Context c;
	FSContext context;
	TransactionsJournal journal;
	File sessionDir;
	
	@Before
	public void init() throws Exception{
		dir.mkdirs();
		
		InitParams params = new InitParams(dir, 2, 2, 2);
		InitContextExt init = new InitContextExt(params, dir, null);
		
		c = new Context(init, null, null, null,null, null);
		journal = new TransactionsJournal();
		ContainerStore store = new ContainerStore();
		//context = new FSContext(null, store, c,journal);
		store.init(context);
		journal.init(context);
		sessionDir = journal.getSessionDir();
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
	
	@Test
	public void saveContainerTest() throws Exception{
		
		File file = new File(dir,"test-container.xml");
		final Container container = Container.create(file, c);
		
		new Transaction<Void>(context) {

			@Override
			protected Void body() throws Throwable {
				invoke(new SaveContainer(container));
				return null;
			}
		}.start();
		
		new Transaction<Void>(context) {

			@Override
			protected Void body() throws Throwable {
				invoke(new SaveContainer(container));
				return null;
			}
		}.start();
		
		File tr1 = new File(sessionDir,"tr-1");
		assertEquals(true, tr1.exists());
		
		File tr2 = new File(sessionDir,"tr-2");
		assertEquals(true, tr2.exists());
		

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
