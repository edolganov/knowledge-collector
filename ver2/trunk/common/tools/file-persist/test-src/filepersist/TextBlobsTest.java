package filepersist;

import java.io.File;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.kc.model.Dir;
import ru.kc.model.Node;
import ru.kc.model.Text;
import ru.kc.tools.filepersist.Factory;
import ru.kc.tools.filepersist.TextService;
import ru.kc.tools.filepersist.Tree;
import ru.kc.tools.filepersist.Updater;
import ru.kc.tools.filepersist.impl.InitParams;
import ru.kc.tools.filepersist.impl.PersistServiceImpl;
import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.Blobs;
import ru.kc.tools.filepersist.update.UpdateName;
import ru.kc.tools.filepersist.update.UpdateText;
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
		Updater updater = ps.updater();
		
		Node root = tree.getRoot();
		updater.update((Text)root, new UpdateText("test"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void createTextForSavedNodeException() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		TextService textService = ps.textService();
		Factory factory = ps.factory();
		
		Text text = factory.createText("text", null);
		textService.getText(text);
	}
	
	@Test
	public void createNullText() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Updater updater = ps.updater();
		Factory factory = ps.factory();
		
		Node root = tree.getRoot();
		Text text = factory.createText("test", null);
		tree.add(root, text);
		
		updater.update(text, new UpdateText(null));
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
		Updater updater = ps.updater();
		Factory factory = ps.factory();
		
		Node root = tree.getRoot();
		Text text = factory.createText("test", null);
		tree.add(root, text);
		
		updater.update(text, new UpdateText("тест"));
		String savedContent = ps.textService().getText(text);
		assertEquals("тест", savedContent);
	}
	
	
	@Test
	public void deleteText() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Updater updater = ps.updater();
		Factory factory = ps.factory();
		TextService textService = ps.textService();
		
		Node root = tree.getRoot();
		Text text = factory.createText("test", null);
		tree.add(root, text);
		
		updater.update(text, new UpdateText("тест"));
		String savedContent = textService.getText(text);
		assertEquals("тест", savedContent);
		
		updater.update(text, new UpdateText(null));
		String nullContent = textService.getText(text);
		assertEquals(null, nullContent);
	}
	
	@Test
	public void createTextSubNodes() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		TextService textService = ps.textService();
		Updater updater = ps.updater();
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
		
		updater.update(text, new UpdateText("суб тест"));
		String savedContent = textService.getText(text);
		assertEquals("суб тест", savedContent);
	}
	
	
	
	@Test
	public void deleteTextWithNodes() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Updater updater = ps.updater();
		Factory factory = ps.factory();
		
		Node root = tree.getRoot();
		Text child = factory.createText("child", null);
		tree.add(root, child);
		//add many other child
		for(int i =2; i < 13; i++){
			tree.add(root, factory.createDir("child"+i, null));
		}
		Text subChild = factory.createText("subChild", null);
		tree.add(child, subChild);
		Text subSubChild = factory.createText("test", null);
		tree.add(subChild, subSubChild);
		
		updater.update(child, new UpdateText("тест1"));
		updater.update(subChild, new UpdateText("тест2"));
		updater.update(subSubChild, new UpdateText("тест3"));
		
		File text1 = getTextFile(child);
		File text2 = getTextFile(subChild);
		File text3 = getTextFile(subSubChild);
		
		assertEquals(true, text1.exists());
		assertEquals(true, text2.exists());
		assertEquals(true, text3.exists());
		
		tree.deleteRecursive(child);
		
		assertEquals(false, text1.exists());
		assertEquals(false, text2.exists());
		assertEquals(false, text3.exists());
	}
	
	@Test
	public void createTextWithUpdateInterface() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		Updater updater = ps.updater();
		
		Node root = tree.getRoot();
		Text text = factory.createText("test", null);
		tree.add(root, text);
		
		updater.update(text, new UpdateText("тест"));
		
		String savedContent = text.getText();
		assertEquals("тест", savedContent);
	}
	
	@Test
	public void deleteTextWithUpdateInterface() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		Updater updater = ps.updater();
		
		Node root = tree.getRoot();
		Text text = factory.createText("test", null);
		tree.add(root, text);
		
		updater.update(text, new UpdateText("тест"));
		
		String savedContent = text.getText();
		assertEquals("тест", savedContent);
		
		updater.update(text, new UpdateText(null));
		String nullContent = text.getText();
		assertEquals(null, nullContent);
	}
	
	@Test
	public void revertAllUpdatesWithBrokenTextUpdate() throws Exception{
		PersistServiceImpl ps = createService(2,2,2);
		Tree tree = ps.tree();
		Factory factory = ps.factory();
		Updater updater = ps.updater();
		
		Node root = tree.getRoot();
		Dir dir = factory.createDir("test", null);
		tree.add(root, dir);
		
		try {
			updater.update(dir, new UpdateName("renamed"), new UpdateText("тест"));
			fail(IllegalArgumentException.class+" expected");
		}catch (IllegalArgumentException e) {
			//good
		}
		
		dir = (Dir)root.getChildren().get(0);
		assertEquals("test", dir.getName());
	}
	

	private File getTextFile(Text child) {
		NodeBean bean = ((NodeBean)child);
		Blobs blobs = bean.getContainer().getContext().fs.getContext().blobs;
		File file = blobs.getTextPath(bean);
		return file;
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
