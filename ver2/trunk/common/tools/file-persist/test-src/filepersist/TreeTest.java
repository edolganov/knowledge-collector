package filepersist;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.kc.model.Dir;
import ru.kc.model.Link;
import ru.kc.model.Node;
import ru.kc.tools.filepersist.Factory;
import ru.kc.tools.filepersist.Tree;
import ru.kc.tools.filepersist.impl.InitParams;
import ru.kc.tools.filepersist.impl.PersistServiceImpl;
import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.model.impl.NodeBean;
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
	public void firstCreateDataStructure() throws Exception {
		InitParams init = new InitParams(dir, 10, 10, 10);
		PersistServiceImpl ps = new PersistServiceImpl();
		ps.init(init);
		
		File rootFile = new File(dir.getPath()+"/nodes/000.xml");
		assertEquals(true, rootFile.exists());
		
		long lastModified = rootFile.lastModified();
		
		PersistServiceImpl ps2 = new PersistServiceImpl();
		ps2.init(init);
		ps2.tree();
		assertEquals(true, lastModified == rootFile.lastModified());
		

	}
	
	@Test
	public void getEmptyChildrenList()throws Exception{
		PersistServiceImpl ps = createService(2,10,10);
		Tree tree = ps.tree();
		Node root = tree.getRoot();
		List<Node> children = root.getChildren();
		assertEquals(0, children.size());
	}

	@Test
	public void addNode() throws Exception{
		PersistServiceImpl ps = createService(2,10,10);
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
	}
	

	@Test
	public void loadNodes() throws Exception{
		//create data
		PersistServiceImpl ps_ = createService(2,2,2);
		Node root_ = ps_.tree().getRoot();
		
		int size = 8;
		for(int i=0; i<size; ++i){
			ps_.tree().add(root_, ps_.factory().createLink("test"+(i+1),null,null));
		}
		List<Node> children_ = ps_.tree().getRoot().getChildren();
		for(int i=0; i<size; ++i){
			ps_.tree().add(children_.get(0), ps_.factory().createDir("test"+(i+1),null));
			ps_.tree().add(children_.get(1), ps_.factory().createFileLink("test"+(i+1),null,null));
		}
		
		//check data loading from fs
		PersistServiceImpl ps = createService(2,2,2);
		Node root = ps.tree().getRoot();
		List<Node> children = root.getChildren();
		assertEquals(true, children.size() == size);
		for(int i=0; i<size; ++i) assertEquals("test"+(i+1), children.get(i).getName());
		for(int i=0; i<size; ++i) assertEquals(size, children.get(0).getChildren().size());
		for(int i=0; i<size; ++i) assertEquals(size, children.get(1).getChildren().size());
		
		
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void tryRemoveRoot() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		
		Node root = tree.getRoot();
		tree.deleteRecursive(root);
	}
	
	
	@Test(expected=IllegalStateException.class)
	public void tryDeletedNodeWithoutParent() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		
		Dir child = factory.createDir("child", null);
		tree.deleteRecursive(child);
	}
	
	
	@Test 
	public void removeNode() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		
		Node root = tree.getRoot();
		Dir child = factory.createDir("child", null);
		
		tree.add(root, child);
		tree.deleteRecursive(child);
		
		PersistServiceImpl newPs = createService(2,2,2);
		Tree newTree = newPs.tree();
		
		Node newRoot = newTree.getRoot();
		List<Node> children = newRoot.getChildren();
		assertEquals(0, children.size());
		
	}
	
	@Test 
	public void remove_node_recursive_from_root() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		
		Node root = tree.getRoot();
		Dir child = factory.createDir("child", null);
		Dir subChild1 = factory.createDir("sub-1", null);
		Dir subChild2 = factory.createDir("sub-2", null);
		Dir subSubChild1 = factory.createDir("sub-sub-1", null);
		Dir subSubChild2 = factory.createDir("sub-sub-2", null);
		
		
		tree.add(root, child);
		tree.add(child, subChild1);
		tree.add(child, subChild2);
		tree.add(subChild2, subSubChild1);
		tree.add(subChild2, subSubChild2);
		tree.deleteRecursive(child);
		
		PersistServiceImpl newPs = createService(2,2,2);
		Tree newTree = newPs.tree();
		
		Node newRoot = newTree.getRoot();
		assertEquals(0, ((NodeBean)newRoot).getChildrenIds().size());
		List<Node> children = newRoot.getChildren();
		assertEquals(0, children.size());
		
	}
	
	@Test 
	public void remove_sub_node_recursive_from_node() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		
		Node root = tree.getRoot();
		Dir node = factory.createDir("node", null);
		Dir child = factory.createDir("child", null);
		Dir subChild1 = factory.createDir("sub-1", null);
		Dir subChild2 = factory.createDir("sub-2", null);
		Dir subSubChild1 = factory.createDir("sub-sub-1", null);
		Dir subSubChild2 = factory.createDir("sub-sub-2", null);
		
		
		tree.add(root, node);
		tree.add(node, child);
		tree.add(child, subChild1);
		tree.add(child, subChild2);
		tree.add(subChild2, subSubChild1);
		tree.add(subChild2, subSubChild2);
		tree.deleteRecursive(child);
		
		PersistServiceImpl newPs = createService(2,2,2);
		Tree newTree = newPs.tree();
		
		Node newRoot = newTree.getRoot();
		assertEquals(1, newRoot.getChildren().size());
		
		Node newNode = newRoot.getChildren().get(0);
		//check for no children
		assertEquals(0, ((NodeBean)newNode).getChildrenIds().size());
		List<Node> children = newNode.getChildren();
		assertEquals(0, children.size());
		
	}
	
	
	@Test
	public void nullContainerAfterDelete() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		
		Node root = tree.getRoot();
		Dir child1 = factory.createDir("child-1", null);
		
		tree.add(root, child1);
		tree.deleteRecursive(child1);
		
		Container child1Container = toNodeBean(child1).getContainer();
		assertEquals(null, child1Container);
	}
	
	
	@Test 
	public void addNodeAfterDelete() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		
		Node root = tree.getRoot();
		Dir child1 = factory.createDir("child-1", null);
		Dir child2 = factory.createDir("child-2", null);
		
		tree.add(root, child1);
		tree.add(root, child2);
		
		Container child2Container = toNodeBean(child2).getContainer();
		tree.deleteRecursive(child2);
		
		Dir child3 = factory.createDir("child-3", null);
		tree.add(root, child3);
		Container child3Container = toNodeBean(child3).getContainer();
		
		assertEquals(child2Container, child3Container);
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
	
	private NodeBean toNodeBean(Node node) {
		return (NodeBean) node;
	}
	



}
