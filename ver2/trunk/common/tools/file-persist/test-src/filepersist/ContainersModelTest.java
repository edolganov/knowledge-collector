package filepersist;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.kc.tools.filepersist.impl.Context;
import ru.kc.tools.filepersist.impl.InitContextExt;
import ru.kc.tools.filepersist.impl.InitParams;
import ru.kc.tools.filepersist.impl.Listeners;
import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.model.impl.TextBean;
import ru.kc.tools.filepersist.persist.ContainerStore;
import ru.kc.tools.filepersist.persist.FSContext;
import ru.kc.tools.filepersist.persist.model.ContainersModel;
import ru.kc.util.file.FileUtil;

import junit.framework.Assert;

public class ContainersModelTest extends Assert {
	
	File dir = new File("./test_data");
	FSContext context;
	
	@Before
	public void init() throws Exception{
		InitParams params = new InitParams(dir, 2, 2, 2);
		InitContextExt init = new InitContextExt(params, dir, null);
		Context c = new Context(init, null, null, null, null, new Listeners());
		
		ContainerStore store = new ContainerStore();
		context = new FSContext(null, store, c);
		store.init(context);
	}
	
	@After
	public void deleteDir() {
		FileUtil.deleteDirRecursive(dir);
	}
	
	@Test
	public void createTree(){
		ContainersModel model = new ContainersModel();
		model.init(context);
		
		Container root = model.createRootContainer();
		assertEquals("./test_data/000.xml", getPath(root));
		model.setRoot(root);
		
		assertEquals("./test_data/000.xml", getNextContainerPath(model));
		assertEquals("./test_data/001.xml", getNextContainerPath(model));
		assertEquals("./test_data/000/000.xml", getNextContainerPath(model));
		assertEquals("./test_data/000/001.xml", getNextContainerPath(model));
		assertEquals("./test_data/001/000.xml", getNextContainerPath(model));
		assertEquals("./test_data/001/001.xml", getNextContainerPath(model));
		assertEquals("./test_data/000/000/000.xml", getNextContainerPath(model));
		assertEquals("./test_data/000/000/001.xml", getNextContainerPath(model));
		assertEquals("./test_data/000/001/000.xml", getNextContainerPath(model));
		assertEquals("./test_data/000/001/001.xml", getNextContainerPath(model));
		assertEquals("./test_data/001/000/000.xml", getNextContainerPath(model));
		assertEquals("./test_data/001/000/001.xml", getNextContainerPath(model));
		assertEquals("./test_data/001/001/000.xml", getNextContainerPath(model));
		assertEquals("./test_data/001/001/001.xml", getNextContainerPath(model));
		assertEquals("./test_data/000/000/000/000.xml", getNextContainerPath(model));


	}



	private String getNextContainerPath(ContainersModel model) {
		Container notFullContainer = model.getNotFullContainer();
		Container fillContainer = fillContainer(notFullContainer);
		return getPath(fillContainer);
	}


	private String getPath(Container container) {
		return container.getFile().getPath().replace("\\", "/");
	}
	
	private Container fillContainer(Container container) {
		int maxSize = container.getMaxSize();
		for (int i = 0; i < maxSize; i++) {
			TextBean node = new TextBean();
			node.setId(""+i);
			container.add(node);
		}
		return container;
	}

}
