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
import ru.kc.util.file.FileUtil;

public class UpdaterTest extends Assert {
	
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
	public void renameRootForNewServiceInstance() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		Updater updater = ps.updater();
		
		
		String newName = "newName";
		Node root = tree.getRoot();
		tree.add(root, factory.createDir("child1", null));
		tree.add(root, factory.createDir("child2", null));
		updater.updateName(root, newName);
		
		PersistServiceImpl newPs = createService(2,2,2);
		Tree newTree = newPs.tree();
		Node newRoot = newTree.getRoot();
		
		assertEquals(newName, newRoot.getName());
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
