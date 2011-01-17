package filepersist;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.kc.model.Link;
import ru.kc.model.Node;
import ru.kc.tools.filepersist.Factory;
import ru.kc.tools.filepersist.InitParams;
import ru.kc.tools.filepersist.PeristService;
import ru.kc.tools.filepersist.Tree;
import ru.kc.util.file.FileUtil;
import static org.junit.Assert.* ;


public class TreeTest {
	
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
	public void firstCreate() throws Exception {
		FileUtil.deleteDirRecursive(dir);
		InitParams init = new InitParams(dir, 10, 10, 10);
		PeristService ps = new PeristService();
		ps.init(init);
		
		File rootFile = new File(dir.getPath()+"/nodes/000.xml");
		assertEquals(true, rootFile.exists());
		
		long lastModified = rootFile.lastModified();
		
		PeristService ps2 = new PeristService();
		ps2.init(init);
		ps2.tree();
		assertEquals(true, lastModified == rootFile.lastModified());
		

	}

	@Test
	public void createNode() throws Exception{
		FileUtil.deleteDirRecursive(dir);
		PeristService ps = createService(10);
		Tree tree = ps.tree();
		Factory factory = ps.factory();

		Node root = tree.getRoot();
		Link child = factory.createLink("test",null,null);
		tree.add(root, child);
		
		
	}
	
	private PeristService createService(int countElements){
		InitParams init = new InitParams(dir, countElements, countElements, countElements);
		PeristService ps = new PeristService();
		try {
			ps.init(init);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		return ps;
	}
	



}
