package scriptengine;

import java.io.File;
import java.util.Set;

import org.apache.log4j.Level;
import org.junit.Test;

import ru.kc.tools.scriptengine.ScriptId;
import ru.kc.tools.scriptengine.ScriptServiceController;
import ru.kc.tools.scriptengine.ScriptsService;
import ru.kc.util.log4j.Log4JConfig;

import junit.framework.Assert;

public class ScriptServiceTest extends Assert implements ScriptServiceController {
	
	static {
		Log4JConfig.defaultConfig(Log4JConfig.JBOSS_PATTERN, Level.WARN);
	}
	
	@Test
	public void invoke() throws Exception{
		ScriptsService service = createService();
		Set<Object> names = service.getNamesByDomain("test");
		assertEquals(2, names.size());
		
		assertEquals("1",service.createInstance("test", "TestScript").invoke("getText"));
		assertEquals("2",service.createInstance("test", "TestScript2").invoke("getText"));
		
	}

	private ScriptsService createService() {
		ScriptsService out = new ScriptsService(this);
		File dir = new File("./common/tools/script-engine/test-src/scriptengine/scripts");
		out.addCodeBase(dir);
		return out;
	}

	@Override
	public ScriptId getId(Class<?> type) {
		Mapping annotation = type.getAnnotation(Mapping.class);
		if(annotation == null) return null;
		
		return new ScriptId(annotation.value(), type.getSimpleName());
	}

}
