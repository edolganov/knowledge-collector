package filepersist;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import ru.kc.tools.filepersist.impl.InitContext;
import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.model.impl.TextBean;
import ru.kc.tools.filepersist.persist.FSContext;
import ru.kc.tools.filepersist.persist.model.ContainersModel;

import junit.framework.Assert;

public class ContainersModelTest extends Assert {
	
	File dir = new File("./test_data");
	FSContext context;
	
	@Before
	public void init() throws Exception{
		context = new FSContext(null, null, null, new InitContext(dir, null, 2, 2, 2));
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
			container.add(new TextBean());
		}
		return container;
	}

}
