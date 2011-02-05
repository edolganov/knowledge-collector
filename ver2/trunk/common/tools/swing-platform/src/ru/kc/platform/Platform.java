package ru.kc.platform;

import java.io.File;

import ru.kc.platform.app.App;
import ru.kc.util.log4j.Log4JConfig;

public class Platform {
	
	private static File dataDir;


	public static void setDataDir(File dataDir) {
		Platform.dataDir = dataDir;
		dataDir.mkdir();
	}


	public static void enableLogFile() throws Exception {
		String fileName = "app.log";
		File logFile = new File(dataDir,fileName);
		logFile.delete();
		Log4JConfig.defaultConfig(Log4JConfig.JBOSS_PATTERN,logFile.getPath());
	}


	public static App createApp() {
		return new App();
	}


}
