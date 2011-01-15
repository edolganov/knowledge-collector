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
	File rootFile = new File(dir.getPath()+"/nodes/000.xml");
	PeristService ps;
	Tree tree;
	Factory factory;
	
	@Before
	public void init() throws Exception{
		FileUtil.deleteDirRecursive(dir);
		dir.mkdirs();
		
		InitParams init = new InitParams(dir, 10, 10, 10);
		PeristService ps = new PeristService();
		ps.init(init);
		
		tree = ps.tree();
		factory = ps.factory();
	}
	
	@After
	public void deleteDir() {
		//FileUtil.deleteDirRecursive(dir);
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

	@Test
	public void createNode() throws Exception{
//		Node root = tree.getRoot();
//		Link child = factory.createLink("test",null,null);
//		tree.add(root, child);
		
	}
	



}
