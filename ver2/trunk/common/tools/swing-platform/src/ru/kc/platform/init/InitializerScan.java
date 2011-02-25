package ru.kc.platform.init;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public class InitializerScan {
	
	private static final Log log = LogFactory.getLog(InitializerScan.class);
	
	public void scanAndInvokeFirstInitializers(String packagePreffix){
		
		Reflections reflections = new Reflections(new ConfigurationBuilder()
        .setUrls(ClasspathHelper.getUrlsForPackagePrefix(packagePreffix))
        .setScanners(new SubTypesScanner())
		.filterInputsBy(new FilterBuilder.Include(FilterBuilder.prefix(packagePreffix))));
		
		
		Set<Class<? extends BeforeStartInitializer>> set = reflections.getSubTypesOf(BeforeStartInitializer.class);
		log.info("found "+set.size()+" types of BeforeStartInitializer");
		for (Class<? extends BeforeStartInitializer> type : set) {
			log.info("create and invoke instance of "+type);
			try {
				((BeforeStartInitializer)type.newInstance()).doAction();
			} catch (Exception e) {
				log.error("", e);
			}
		}
		
		
	}

}
