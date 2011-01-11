package filepersist;

import java.io.File;

import ru.kc.tools.filepersist.PersistService;
import ru.kc.util.file.FileUtil;
import junit.framework.TestCase;


public class NodesCRUD extends TestCase {
	
	File dir = new File("./test_data");
	File rootFile = new File(dir.getPath()+"/nodes/000.xml");
	
	
	public void testFirstCreate() throws Exception {
		PersistService service = new PersistService();
		service.init(dir.getPath());
		assertEquals(true, rootFile.exists());
		
		long lastModified = rootFile.lastModified();
		PersistService service2 = new PersistService();
		service2.init(dir.getPath());
		assertEquals(true, lastModified == rootFile.lastModified());
		
		deleteDir();
	}


	private void deleteDir() {
		FileUtil.deleteDirRecursive(dir);
	}

}
