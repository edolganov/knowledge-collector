package ru.kc.util.log4j;

import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class Log4JConfig {
	
	public static final String STANDART_PATTERN = "%-5p [%t]: %m%n";
	public static final String JBOSS_PATTERN = "%d{ABSOLUTE} %-5p [%c{1}] %m%n";
	
	public static void defaultConfig(String patternLayuot, String fileName) throws IOException{
	    PatternLayout layout = new PatternLayout(patternLayuot);
	    
		Logger rootLogger = Logger.getRootLogger();
		  if (!rootLogger.getAllAppenders().hasMoreElements()) {
		      rootLogger.setLevel(Level.INFO);

		      rootLogger.addAppender(new ConsoleAppender(layout));
		      if(fileName != null){
			      rootLogger.addAppender(new FileAppender(layout, fileName, true));
		      }

		  }
	}

}
