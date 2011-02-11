package ru.kc.platform.global;

import java.util.HashMap;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import ru.kc.platform.annotations.GlobalMapping;

public class GlobalObjects {
	
	private static final Log log = LogFactory.getLog(GlobalObjects.class);
	
	private HashMap<String, Class<?>> globalTypes = new HashMap<String, Class<?>>();
	
	public void scan(String packagePreffix){
		if(packagePreffix == null) 
			throw new IllegalArgumentException("packagePreffix is null");
		
		Reflections reflections = new Reflections(new ConfigurationBuilder()
        .setUrls(ClasspathHelper.getUrlsForPackagePrefix(packagePreffix))
        .setScanners(new TypeAnnotationsScanner())
		.filterInputsBy(new FilterBuilder.Include(FilterBuilder.prefix(packagePreffix))));
		
		HashMap<String, Class<?>> newTypes = new HashMap<String, Class<?>>();
		Set<Class<?>> all = reflections.getTypesAnnotatedWith(GlobalMapping.class);
		for (Class<?> type : all) {
			GlobalMapping annotation = type.getAnnotation(GlobalMapping.class);
			String mapping = annotation.value();
			newTypes.put(mapping, type);
		}
		
		globalTypes.putAll(newTypes);
		
		log.info("found "+newTypes.size()+" global objects: "+newTypes);
	}
	
	@SuppressWarnings("unchecked")
	public <N> N instanceByMapping(String mapping){
		try {
			Class<?> type = globalTypes.get(mapping);
			if(type == null) 
				throw new IllegalStateException("no type by mapping "+mapping);
			return (N) type.newInstance();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		} 
	}

}
