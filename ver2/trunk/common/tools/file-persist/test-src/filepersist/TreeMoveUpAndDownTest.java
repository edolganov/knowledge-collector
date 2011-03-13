package filepersist;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.kc.model.Dir;
import ru.kc.model.Node;
import ru.kc.tools.filepersist.Factory;
import ru.kc.tools.filepersist.Tree;
import ru.kc.tools.filepersist.impl.InitParams;
import ru.kc.tools.filepersist.impl.PersistServiceImpl;
import ru.kc.util.file.FileUtil;
import org.junit.Assert;


public class TreeMoveUpAndDownTest extends Assert{
	
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
	public void moveRoot() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Node root = tree.getRoot();
		int newIndex = tree.moveUp(root);
		assertEquals(0, newIndex);
		
		newIndex = tree.moveDown(root);
		assertEquals(0, newIndex);
	}
	
	@Test 
	public void moveOneChild() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		
		Node root = tree.getRoot();
		Dir child = factory.createDir("child", null);
		
		tree.add(root, child);
		int newIndex = tree.moveUp(child);
		assertEquals(0, newIndex);
	}
	
	
	@Test 
	public void moveChildUpInCycle() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		
		Node root = tree.getRoot();
		Dir child1 = factory.createDir("child1", null);
		Dir child2 = factory.createDir("child2", null);
		Dir child3 = factory.createDir("child3", null);
		
		tree.add(root, child1);
		tree.add(root, child2);
		tree.add(root, child3);
		int newIndex = tree.moveUp(child1);
		assertEquals(2, newIndex);
		newIndex = tree.moveUp(child1);
		assertEquals(1, newIndex);
		newIndex = tree.moveUp(child1);
		assertEquals(0, newIndex);
	}
	
	@Test 
	public void moveChildDownInCycle() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		
		Node root = tree.getRoot();
		Dir child1 = factory.createDir("child1", null);
		Dir child2 = factory.createDir("child2", null);
		Dir child3 = factory.createDir("child3", null);
		
		tree.add(root, child1);
		tree.add(root, child2);
		tree.add(root, child3);
		int newIndex = tree.moveDown(child1);
		assertEquals(1, newIndex);
		newIndex = tree.moveDown(child1);
		assertEquals(2, newIndex);
		newIndex = tree.moveDown(child1);
		assertEquals(0, newIndex);
	}
	
	@Test 
	public void moveChildInMiddle() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		
		Node root = tree.getRoot();
		Dir child1 = factory.createDir("child1", null);
		Dir child2 = factory.createDir("child2", null);
		Dir child3 = factory.createDir("child3", null);
		
		tree.add(root, child1);
		tree.add(root, child2);
		tree.add(root, child3);
		int newIndex = tree.moveDown(child1);
		assertEquals(1, newIndex);
		
		
		PersistServiceImpl newPs = createService(2,2,2);
		Tree newTree = newPs.tree();
		Node newRoot = newTree.getRoot();
		List<Node> children = newRoot.getChildren();
		assertEquals(3, children.size());
		assertEquals("child2", children.get(0).getName());
		assertEquals("child1", children.get(1).getName());
		assertEquals("child3", children.get(2).getName());
	}
	
	
	
	
	


	private PersistServiceImpl createService(int maxNodesInContainer,
			int maxContainerFilesInFolder, int maxFoldersInLevel){
		InitParams init = new InitParams(dir, maxNodesInContainer, maxContainerFilesInFolder, maxFoldersInLevel);
		PersistServiceImpl ps = new PersistServiceImpl();
		try {
			ps.init(init);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		return ps;
	}
	



}
