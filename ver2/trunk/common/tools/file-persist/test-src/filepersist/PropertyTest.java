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
		
		
		updater.update(tree.getRoot(), new SetProperty("key", "value"));
		assertEquals("value", tree.getRoot().getProperty("key"));
		
		PersistServiceImpl newPs = createService(2,2,2);
		Tree newTree = newPs.tree();
		assertEquals("value", newTree.getRoot().getProperty("key"));
		
		
		updater.update(tree.getRoot(), new RemoveProperty("key"));
		assertEquals(null, tree.getRoot().getProperty("key"));
		assertEquals("value", newTree.getRoot().getProperty("key"));
		
		PersistServiceImpl newPs2 = createService(2,2,2);
		Tree newTree2 = newPs2.tree();
		Node newRoot2 = newTree2.getRoot();
		assertEquals(null, newRoot2.getProperty("key"));
	}
	
	@Test
	public void setPropertyToRootChild() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		Updater updater = ps.updater();
		
		Dir dir = factory.createDir("dir", null);
		tree.add(tree.getRoot(), dir);
		
		updater.update(dir, new SetProperty("key", "value"));
		assertEquals("value", tree.getRoot().getChildren().get(0).getProperty("key"));
		
		PersistServiceImpl newPs = createService(2,2,2);
		Tree newTree = newPs.tree();
		assertEquals("value", newTree.getRoot().getChildren().get(0).getProperty("key"));
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
