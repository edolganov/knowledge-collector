package filepersist;

import java.io.File;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.kc.model.Node;
import ru.kc.model.Text;
import ru.kc.tools.filepersist.Factory;
import ru.kc.tools.filepersist.TextService;
import ru.kc.tools.filepersist.Tree;
import ru.kc.tools.filepersist.impl.InitParams;
import ru.kc.tools.filepersist.impl.PersistServiceImpl;
import ru.kc.util.file.FileUtil;

public class TextBlobsTest extends Assert {
	
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
	
	@Test(expected=ClassCastException.class)
	public void createTextForRootException() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		TextService textService = ps.textService();
		
		Node root = tree.getRoot();
		textService.setText((Text)root, "test");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void createTextForSavedNodeException() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		TextService textService = ps.textService();
		Factory factory = ps.factory();
		
		Text text = factory.createText("text", null);
		textService.getText(text);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void createNullTextException() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		TextService textService = ps.textService();
		Factory factory = ps.factory();
		
		Node root = tree.getRoot();
		Text text = factory.createText("test", null);
		tree.add(root, text);
		
		textService.setText(text, null);
	}
	
	@Test
	public void getNullTextForCreatedNode() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		TextService textService = ps.textService();
		Factory factory = ps.factory();
		
		Node root = tree.getRoot();
		Text text = factory.createText("test", null);
		tree.add(root, text);
		
		String content = textService.getText(text);
		assertEquals(null, content);
	}
	

	
	
	@Test
	public void createText() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		TextService textService = ps.textService();
		Factory factory = ps.factory();
		
		Node root = tree.getRoot();
		Text text = factory.createText("test", null);
		tree.add(root, text);
		
		textService.setText(text, "тест");
		String savedContent = textService.getText(text);
		assertEquals("тест", savedContent);
	}
	
	@Test
	public void createTextSubNodes() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		TextService textService = ps.textService();
		Factory factory = ps.factory();
		
		Node root = tree.getRoot();
		Node child = factory.createDir("child", null);
		tree.add(root, child);
		//add many other child
		for(int i =2; i < 13; i++){
			tree.add(root, factory.createDir("child"+i, null));
		}
		
		Node subChild = factory.createDir("subChild", null);
		tree.add(child, subChild);
		
		
		Text text = factory.createText("test", null);
		tree.add(subChild, text);
		
		textService.setText(text, "суб тест");
		String savedContent = textService.getText(text);
		assertEquals("суб тест", savedContent);
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
