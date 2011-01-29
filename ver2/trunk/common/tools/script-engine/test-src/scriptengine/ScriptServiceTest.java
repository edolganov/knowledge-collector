package scriptengine;

import java.io.File;
import java.util.List;

import org.apache.log4j.Level;
import org.junit.Test;

import ru.kc.tools.scriptengine.ScriptServiceController;
import ru.kc.tools.scriptengine.ScriptsService;
import ru.kc.util.log4j.Log4JConfig;

import junit.framework.Assert;

public class ScriptServiceTest extends Assert implements ScriptServiceController {
	
	static {
		Log4JConfig.defaultConfig(Log4JConfig.JBOSS_PATTERN, Level.OFF);
	}
	
	@Test
	public void invoke() throws Exception{
		ScriptsService service = createService();
		List<String> list = service.getTypesByMapping("test");
		assertEquals(2, list.size());
		
		assertEquals("1",service.createInstance("test", list.get(0)).invoke("getText"));
		assertEquals("2",service.createInstance("test", list.get(1)).invoke("getText"));
		
	}

	private ScriptsService createService() {
		ScriptsService out = new ScriptsService(this);
		File dir = new File("./common/tools/script-engine/test-src/scriptengine/scripts");
		out.addCodeBase(dir);
		return out;
	}

	@Override
	public Object getMapping(Class<?> type) {
		Mapping annotation = type.getAnnotation(Mapping.class);
		return annotation != null? annotation.value() : null;
	}

}
