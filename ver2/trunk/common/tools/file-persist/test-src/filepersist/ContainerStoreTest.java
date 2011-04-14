package filepersist;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.kc.tools.filepersist.impl.Context;
import ru.kc.tools.filepersist.impl.InitContextExt;
import ru.kc.tools.filepersist.impl.InitParams;
import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.persist.ContainerStore;
import ru.kc.tools.filepersist.persist.FSContext;
import ru.kc.tools.filepersist.persist.transaction.InterceptorsManager;
import ru.kc.util.file.FileUtil;

public class ContainerStoreTest extends Assert  {
	
	File dir = new File("./test_data");
	FSContext context;
	
	@Before
	public void init() throws Exception{
		FileUtil.deleteDirRecursive(dir);
		dir.mkdirs();
		
		context = testContext();

	}
	
	@After
	public void deleteDir() {
		FileUtil.deleteDirRecursive(dir);
	}
	
	@Test
	public void saveAndRollback() throws Exception{
		Container container = new Container();
		container.init(new File(dir,"test.xml"), context.c);
		assertEquals(0,container.getRevision());
		
		ContainerStore store = new ContainerStore();
		store.init(testContext());
		
		store.save(container);
		assertEquals(1,container.getRevision());
		assertEquals(false, new File("./test_data/.hist/test.xml.0").exists());

		
		store.save(container);
		assertEquals(2,container.getRevision());
		assertEquals(true, new File("./test_data/.hist/test.xml.1").exists());

		
		store.save(container);
		assertEquals(3,container.getRevision());
		assertEquals(false, new File("./test_data/.hist/test.xml.1").exists());
		assertEquals(true, new File("./test_data/.hist/test.xml.2").exists());

		
		store.rollback(container);
		assertEquals(2,container.getRevision());
		assertEquals(false, new File("./test_data/.hist/test.xml.2").exists());
		
		
		store.save(container);
		assertEquals(3,container.getRevision());
		assertEquals(false, new File("./test_data/.hist/test.xml.1").exists());
		assertEquals(true, new File("./test_data/.hist/test.xml.2").exists());
		
		
		store.rollback(container);
		assertEquals(2,container.getRevision());
		assertEquals(false, new File("./test_data/.hist/test.xml.2").exists());
		
		try {
			store.rollback(container);
			fail("expected IllegalStateException");
		}catch (IllegalStateException e) {}
		assertEquals(2,container.getRevision());
	}

	
	public FSContext testContext() throws Exception{
		InitParams params = new InitParams(dir, 10, 10, 10);
		InitContextExt init = new InitContextExt(params, dir, dir);
		Context c = new Context(init, null, null, null, null, null);
		
		ContainerStore store = new ContainerStore();
		InterceptorsManager interceptorsManager = new InterceptorsManager();
		FSContext fsContext = new FSContext(null, store, c, null, interceptorsManager);
		store.init(fsContext);
		interceptorsManager.init("ru.kc.tools.filepersist");
		return fsContext;
	}
	
}
