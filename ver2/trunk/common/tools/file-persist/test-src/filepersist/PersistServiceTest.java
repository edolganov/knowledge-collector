package filepersist;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.kc.tools.filepersist.InitParams;
import ru.kc.tools.filepersist.PeristService;
import ru.kc.tools.filepersist.Tree;
import ru.kc.tools.filepersist.impl.Context;
import ru.kc.tools.filepersist.impl.InitContextExt;
import ru.kc.tools.filepersist.impl.TreeImpl;
import ru.kc.util.file.FileUtil;
import static org.junit.Assert.* ;


public class PersistServiceTest {
	
	File dir = new File("./test_data");
	File rootFile = new File(dir.getPath()+"/nodes/000.xml");
	Tree service;
	Context context;
	
	@Before
	public void init() throws Exception{
		FileUtil.deleteDirRecursive(dir);
		dir.mkdirs();
		
		InitParams init = new InitParams(dir, 10, 10, 10);
		PeristService ps = new PeristService();
		ps.init(init);
		
		service = ps.tree();
	}
	
	@After
	public void deleteDir() {
		FileUtil.deleteDirRecursive(dir);
	}
	
	
	
	@Test
	public void firstCreate() throws Exception {
		assertEquals(true, rootFile.exists());
		
		long lastModified = rootFile.lastModified();
		
		InitParams init = new InitParams(dir, 10, 10, 10);
		PeristService ps2 = new PeristService();
		ps2.init(init);
		ps2.tree();
		assertEquals(true, lastModified == rootFile.lastModified());
	}

	
	public void createNode(){
//		service.
		
	}
	



}
