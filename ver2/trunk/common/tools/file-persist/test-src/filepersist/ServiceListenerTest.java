package filepersist;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.kc.model.Dir;
import ru.kc.model.Node;
import ru.kc.tools.filepersist.Factory;
import ru.kc.tools.filepersist.Tree;
import ru.kc.tools.filepersist.ServiceAdapter;
import ru.kc.tools.filepersist.Updater;
import ru.kc.tools.filepersist.impl.InitParams;
import ru.kc.tools.filepersist.impl.PersistServiceImpl;
import ru.kc.util.file.FileUtil;


public class ServiceListenerTest extends Assert{
	
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
	public void nodeAdded() throws Exception{
		final Boolean[] added = new Boolean[]{false};
		final Node[] parentInListener = new Node[]{null};
		final Node[] childInListener = new Node[]{null};
		
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		ps.addListener(new ServiceAdapter() {
			
			@Override
			public void onAdded(Node parent, Node child) {
				added[0] = true;
				parentInListener[0] = parent;
				childInListener[0] = child;
			}
		});
		
		Dir child = factory.createDir("test", null);
		Node root = tree.getRoot();
		tree.add(root, child);
		assertEquals(true, added[0]);
		assertEquals(root, parentInListener[0]);
		assertEquals(child, childInListener[0]);
		
	}
	
	
	@Test
	public void rootNodeDeleted() throws Exception{
		final Boolean[] deleted = new Boolean[]{false};
		final Node[] parentInListener = new Node[]{null};
		final Node[] childInListener = new Node[]{null};
		
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		ps.addListener(new ServiceAdapter() {
			
			@Override
			public void onDeletedRecursive(Node parent, Node deletedChild) {
				deleted[0] = true;
				parentInListener[0] = parent;
				childInListener[0] = deletedChild;
			}
		});
		
		Dir child = factory.createDir("child", null);
		Node root = tree.getRoot();
		tree.add(root, child);
		tree.deleteRecursive(child);
		assertEquals(true, deleted[0]);
		assertEquals(root, parentInListener[0]);
		assertEquals(child, childInListener[0]);
		
	}
	
	
	@Test
	public void subNodeDeleted() throws Exception{
		final Boolean[] deleted = new Boolean[]{false};
		final Node[] parentInListener = new Node[]{null};
		final Node[] childInListener = new Node[]{null};
		
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		ps.addListener(new ServiceAdapter() {
			
			@Override
			public void onDeletedRecursive(Node parent, Node deletedChild) {
				deleted[0] = true;
				parentInListener[0] = parent;
				childInListener[0] = deletedChild;
			}
		});
		
		Dir child = factory.createDir("child", null);
		Node root = tree.getRoot();
		tree.add(root, child);
		
		Dir subChild = factory.createDir("subChild", null);
		tree.add(child, subChild);
		
		tree.deleteRecursive(subChild);
		assertEquals(true, deleted[0]);
		assertEquals(child, parentInListener[0]);
		assertEquals(subChild, childInListener[0]);
		
	}
	
	@Test
	public void subNodeDeletedForNewServiceInstance() throws Exception{
		final Boolean[] deleted = new Boolean[]{false};
		final Node[] parentInListener = new Node[]{null};
		final Node[] childInListener = new Node[]{null};
		
		PersistServiceImpl oldPs = createService(2,2,2);
		Tree oldTree = oldPs.tree();
		Factory oldFactory = oldPs.factory();

		Dir child = oldFactory.createDir("child", null);
		Node root = oldTree.getRoot();
		oldTree.add(root, child);
		
		Dir subChild = oldFactory.createDir("subChild", null);
		oldTree.add(child, subChild);
		
		PersistServiceImpl ps = createService(2, 2, 2);
		ps.addListener(new ServiceAdapter() {
			
			@Override
			public void onDeletedRecursive(Node parent, Node deletedChild) {
				deleted[0] = true;
				parentInListener[0] = parent;
				childInListener[0] = deletedChild;
			}
		});
		
		Tree tree = ps.tree();
		Node toDelete = tree.getRoot().getChildren().get(0).getChildren().get(0);
		tree.deleteRecursive(toDelete);
		
		assertEquals(true, deleted[0]);
		assertEquals(child, parentInListener[0]);
		assertEquals(subChild, childInListener[0]);
		
	}
	
	@Test
	public void rootUpdated() throws Exception{
		final Node[] oldNode = new Node[]{null};
		final Node[] updatedNode = new Node[]{null};
		
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Updater updater = ps.updater();
		ps.addListener(new ServiceAdapter(){
			
			@Override
			public void onNodeUpdated(Node old, Node updated) {
				oldNode[0] = old;
				updatedNode[0] = updated;
			}
			
		});
		
		Node root = tree.getRoot();
		String newName = "newName";
		String oldName = root.getName();
		updater.updateName(root, newName);
		
		assertEquals(root, updatedNode[0]);
		assertEquals(false, root == updatedNode[0]);
		assertEquals(true, root == oldNode[0]);
		assertEquals(newName, updatedNode[0].getName());
		assertEquals(oldName, root.getName());
		
	}
	
	
	@Test
	public void nodeUpdated() throws Exception{
		final Node[] oldNode = new Node[]{null};
		final Node[] updatedNode = new Node[]{null};
		
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		Updater updater = ps.updater();
		ps.addListener(new ServiceAdapter(){
			
			@Override
			public void onNodeUpdated(Node old, Node updated) {
				oldNode[0] = old;
				updatedNode[0] = updated;
			}
			
		});
		
		String oldName = "oldName";
		String newName = "newName";
		Dir child = factory.createDir(oldName, null);
		Node root = tree.getRoot();
		tree.add(root, child);
		updater.updateName(child, newName);
		
		assertEquals(child, updatedNode[0]);
		assertEquals(false, child == updatedNode[0]);
		assertEquals(true, child == oldNode[0]);
		assertEquals(newName, updatedNode[0].getName());
		assertEquals(oldName, child.getName());
		
	}
	
	
	@Test
	public void nodeUpdatedForNewServiceInstance() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		Updater updater = ps.updater();
		
		String oldName = "oldName";
		String newName = "newName";
		Dir child = factory.createDir(oldName, null);
		Node root = tree.getRoot();
		tree.add(root, child);
		updater.updateName(child, newName);
		
		PersistServiceImpl newPs = createService(2,2,2);
		Tree newTree = newPs.tree();
		Node newChild = newTree.getRoot().getChildren().get(0);
		
		assertEquals(newName, newChild.getName());
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
