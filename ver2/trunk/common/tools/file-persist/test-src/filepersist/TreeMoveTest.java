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


public class TreeMoveTest extends Assert{
	
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
	public void moveNodeToOldParent() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		
		Node root = tree.getRoot();
		Dir child = factory.createDir("child", null);
		
		tree.add(root, child);
		tree.move(child, root);
		
		PersistServiceImpl newPs = createService(2,2,2);
		Tree newTree = newPs.tree();
		
		Node newRoot = newTree.getRoot();
		List<Node> children = newRoot.getChildren();
		assertEquals(1, children.size());
		assertEquals("child", children.get(0).getName());
	}
	
	@Test(expected=IllegalArgumentException.class) 
	public void tryMoveRootToChild() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		
		Node root = tree.getRoot();
		Dir child = factory.createDir("child", null);
		
		tree.add(root, child);
		tree.move(root, child);
	}
	
	@Test 
	public void moveSubChildToRoot() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		
		Node root = tree.getRoot();
		Dir child = factory.createDir("child", null);
		tree.add(root, child);
		
		Dir subChild = factory.createDir("subChild", null);
		tree.add(child, subChild);
		
		tree.move(subChild, root);
		
		PersistServiceImpl newPs = createService(2,2,2);
		Tree newTree = newPs.tree();
		
		Node newRoot = newTree.getRoot();
		List<Node> children = newRoot.getChildren();
		assertEquals(2, children.size());
		assertEquals("child", children.get(0).getName());
		assertEquals("subChild", children.get(1).getName());
		assertEquals(0, children.get(0).getChildren().size());
		assertEquals(0, children.get(1).getChildren().size());
		
	}
	
	@Test(expected=IllegalArgumentException.class) 
	public void tryMoveChildToSubChild() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		
		Node root = tree.getRoot();
		Dir child = factory.createDir("child", null);
		tree.add(root, child);
		
		Dir subChild = factory.createDir("subChild", null);
		tree.add(child, subChild);
		
		tree.move(child, subChild);
	}
	
	
	@Test 
	public void moveSubChildToAnotherBranch() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		
		Node root = tree.getRoot();
		Dir child = factory.createDir("child", null);
		tree.add(root, child);
		
		Dir subChild = factory.createDir("subChild", null);
		tree.add(child, subChild);
		
		Dir child2 = factory.createDir("child2", null);
		tree.add(root, child2);
		
		
		tree.move(subChild, child2);
		
		PersistServiceImpl newPs = createService(2,2,2);
		Tree newTree = newPs.tree();
		
		Node newRoot = newTree.getRoot();
		List<Node> children = newRoot.getChildren();
		assertEquals(2, children.size());
		assertEquals("child", children.get(0).getName());
		assertEquals("child2", children.get(1).getName());
		assertEquals(0, children.get(0).getChildren().size());
		assertEquals(1, children.get(1).getChildren().size());
		assertEquals("subChild", children.get(1).getChildren().get(0).getName());
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
