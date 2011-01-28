package filepersist;

import java.io.File;
import java.util.List;

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
import org.junit.Assert;


public class TreeTest extends Assert{
	
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
		PeristService ps = createService(2,10,10);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		
		Node root = tree.getRoot();
		Link child = factory.createLink("test",null,null);
		tree.add(root, child);
		
		List<Node> children = root.getChildren();
		assertEquals(true, children.size() == 1);
		assertEquals(true, children.get(0) == child);
		
		Link child2 = factory.createLink("test",null,null);
		tree.add(root, child2);
		List<Node> children2 = root.getChildren();
		assertEquals(true, children2.size() == 2);
		assertEquals(true, children2.get(0) == child);
		assertEquals(true, children2.get(1) == child2);

		//create many children
//		for (int i = 0; i < 200; i++) {
//			Node root = tree.getRoot();
//			Link child = factory.createLink("test",null,null);
//			tree.add(root, child);
//		}
	}
	
	@Test
	public void loadNodes() throws Exception{
		FileUtil.deleteDirRecursive(dir);
		//create data
		PeristService ps_ = createService(2,2,2);
		Node root_ = ps_.tree().getRoot();
		
		int size = 8;
		for(int i=0; i<size; ++i){
			ps_.tree().add(root_, ps_.factory().createLink("test"+(i+1),null,null));
		}
		
		//load data
		PeristService ps = createService(2,2,2);
		Tree tree = ps.tree();
		//Factory factory = ps.factory();
		
		Node root = tree.getRoot();
		List<Node> children = root.getChildren();
		assertEquals(true, children.size() == size);
		for(int i=0; i<size; ++i){
			assertEquals("test"+(i+1), children.get(i).getName());
		}
	}
	
	private PeristService createService(int maxNodesInContainer,
			int maxContainerFilesInFolder, int maxFoldersInLevel){
		InitParams init = new InitParams(dir, maxNodesInContainer, maxContainerFilesInFolder, maxFoldersInLevel);
		PeristService ps = new PeristService();
		try {
			ps.init(init);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		return ps;
	}
	

	



}
