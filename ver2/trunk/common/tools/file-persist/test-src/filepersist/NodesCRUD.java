package filepersist;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.kc.tools.filepersist.PersistService;
import ru.kc.util.file.FileUtil;
import static org.junit.Assert.* ;


public class NodesCRUD {
	
	File dir = new File("./test_data");
	File rootFile = new File(dir.getPath()+"/nodes/000.xml");
	PersistService service = new PersistService();
	
	@Before
	public void init() throws Exception{
		FileUtil.deleteDirRecursive(dir);
		dir.mkdirs();
		service.init(dir.getPath());
	}
	
	@After
	public void deleteDir() {
		FileUtil.deleteDirRecursive(dir);
	}
	
	
	
	@Test
	public void firstCreate() throws Exception {
		assertEquals(true, rootFile.exists());
		
		long lastModified = rootFile.lastModified();
		PersistService service2 = new PersistService();
		service2.init(dir.getPath());
		assertEquals(true, lastModified == rootFile.lastModified());
	}

	



}
