package filepersist;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.kc.tools.filepersist.Tree;
import ru.kc.tools.filepersist.impl.InitContext;
import ru.kc.tools.filepersist.impl.TreeImpl;
import ru.kc.util.file.FileUtil;
import static org.junit.Assert.* ;


public class PersistServiceTest {
	
	File dir = new File("./test_data");
	File rootFile = new File(dir.getPath()+"/nodes/000.xml");
	Tree service = new TreeImpl();
	InitContext params = new InitContext(10, 10, 10);
	
	@Before
	public void init() throws Exception{
		FileUtil.deleteDirRecursive(dir);
		dir.mkdirs();
		((TreeImpl)service).init(dir.getPath(),params);
	}
	
	@After
	public void deleteDir() {
		FileUtil.deleteDirRecursive(dir);
	}
	
	
	
	@Test
	public void firstCreate() throws Exception {
		assertEquals(true, rootFile.exists());
		
		long lastModified = rootFile.lastModified();
		TreeImpl service2 = new TreeImpl();
		service2.init(dir.getPath(),params);
		assertEquals(true, lastModified == rootFile.lastModified());
	}

	
	public void createNode(){
//		service.
		
	}
	



}
