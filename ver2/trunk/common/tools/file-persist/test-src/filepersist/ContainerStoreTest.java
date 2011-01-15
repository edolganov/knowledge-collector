package filepersist;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.persist.ContainerStore;
import ru.kc.tools.filepersist.persist.FSContext;
import ru.kc.util.file.FileUtil;

public class ContainerStoreTest extends Assert  {
	
	File dir = new File("./test_data");
	
	@Before
	public void init() throws Exception{
		FileUtil.deleteDirRecursive(dir);
		dir.mkdirs();

	}
	
	@After
	public void deleteDir() {
		FileUtil.deleteDirRecursive(dir);
	}
	
	@Test
	public void saveAndRollback() throws IOException{
		Container container = new Container();
		container.init(new File(dir,"test.xml"), null,10);
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

	
	public FSContext testContext(){
		return new FSContext(null, null, null, null, null, 10, 10, 10);
	}
	
}
