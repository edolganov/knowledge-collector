package filepersist;

import java.io.File;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.kc.model.Dir;
import ru.kc.model.Node;
import ru.kc.tools.filepersist.Factory;
import ru.kc.tools.filepersist.Tree;
import ru.kc.tools.filepersist.Updater;
import ru.kc.tools.filepersist.impl.InitParams;
import ru.kc.tools.filepersist.impl.PersistServiceImpl;
import ru.kc.tools.filepersist.update.RemoveProperty;
import ru.kc.tools.filepersist.update.SetProperty;
import ru.kc.tools.filepersist.update.UpdateName;
import ru.kc.util.file.FileUtil;

public class PropertyTest extends Assert {
	
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
	public void setAndRemovePropertyForRoot() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Updater updater = ps.updater();
		
		
		Node root = tree.getRoot();
		updater.update(root, new SetProperty("key", "value"));
		assertEquals("value", root.getProperty("key"));
		
		PersistServiceImpl newPs = createService(2,2,2);
		Tree newTree = newPs.tree();
		Node newRoot = newTree.getRoot();
		assertEquals("value", newRoot.getProperty("key"));
		
		
		updater.update(root, new RemoveProperty("key"));
		assertEquals(null, root.getProperty("key"));
		assertEquals(null, newRoot.getProperty("key"));
		
		PersistServiceImpl newPs2 = createService(2,2,2);
		Tree newTree2 = newPs2.tree();
		Node newRoot2 = newTree2.getRoot();
		assertEquals(null, newRoot2.getProperty("key"));
	}
	
	//@Test
	public void setAndRemoveProperty() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		Updater updater = ps.updater();
		
		
		String newName = "newName";
		Node root = tree.getRoot();
		
		tree.add(root, factory.createDir("child1", null));
		tree.add(root, factory.createDir("child2", null));
		updater.update(root, new UpdateName(newName));
		
		PersistServiceImpl newPs = createService(2,2,2);
		Tree newTree = newPs.tree();
		Node newRoot = newTree.getRoot();
		
		assertEquals(newName, newRoot.getName());
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
