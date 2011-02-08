package filepersist;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.kc.model.Dir;
import ru.kc.model.Node;
import ru.kc.tools.filepersist.Factory;
import ru.kc.tools.filepersist.InitParams;
import ru.kc.tools.filepersist.PersistService;
import ru.kc.tools.filepersist.Tree;
import ru.kc.tools.filepersist.TreeAdapter;
import ru.kc.util.file.FileUtil;


public class TreeListenerTest extends Assert{
	
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
		FileUtil.deleteDirRecursive(dir);
		
		final Boolean[] added = new Boolean[]{false};
		final Node[] parentInListener = new Node[]{null};
		final Node[] childInListener = new Node[]{null};
		
		PersistService ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		tree.addListener(new TreeAdapter() {
			
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
		assertEquals(root,parentInListener[0]);
		assertEquals(child,childInListener[0]);
		
	}
	
	private PersistService createService(int maxNodesInContainer,
			int maxContainerFilesInFolder, int maxFoldersInLevel){
		InitParams init = new InitParams(dir, maxNodesInContainer, maxContainerFilesInFolder, maxFoldersInLevel);
		PersistService ps = new PersistService();
		try {
			ps.init(init);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		return ps;
	}
	

	



}
