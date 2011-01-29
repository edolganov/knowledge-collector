package scriptengine;

import java.io.File;

import org.apache.log4j.Level;
import org.junit.Test;

import ru.kc.tools.scriptengine.ScriptsService;
import ru.kc.util.log4j.Log4JConfig;

import junit.framework.Assert;

public class ScriptServiceTest extends Assert {
	
	static {
		Log4JConfig.defaultConfig(Log4JConfig.JBOSS_PATTERN, Level.OFF);
	}
	
	@Test
	public void invoke() throws Exception{
		ScriptsService service = createService();
		service.invoke("test", "getText");
		
	}

	private ScriptsService createService() {
		ScriptsService out = new ScriptsService();
		File dir = new File("./common/tools/script-engine/test-src/scriptengine/scripts");
		out.addCodeBase(dir);
		return out;
	}

}
