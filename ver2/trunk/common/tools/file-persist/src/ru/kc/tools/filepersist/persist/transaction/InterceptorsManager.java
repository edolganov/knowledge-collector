package ru.kc.tools.filepersist.persist.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public class InterceptorsManager {
	
	private static Log log = LogFactory.getLog(InterceptorsManager.class);
	
	private HashMap<Class<?>, List<ActionInterceptor>> map = new HashMap<Class<?>, List<ActionInterceptor>>();
	
	public void init(String packagePreffix) throws Exception {
		
		Reflections reflections = new Reflections(new ConfigurationBuilder()
        .setUrls(ClasspathHelper.getUrlsForPackagePrefix(packagePreffix))
        .setScanners(new SubTypesScanner())
		.filterInputsBy(new FilterBuilder.Include(FilterBuilder.prefix(packagePreffix))));
		
		Set<Class<? extends ActionInterceptor>> interceptors = reflections.getSubTypesOf(ActionInterceptor.class);
		//log.info("interceptors count: "+interceptors.size());
		for(Class<? extends ActionInterceptor> interceptorType : interceptors){
			ActionInterceptor instance = interceptorType.newInstance();
			Class<?>[] types = instance.types();
			if(types != null){
				for(Class<?> type : types){
					List<ActionInterceptor> list = map.get(type);
					if(list == null){
						list = new ArrayList<ActionInterceptor>();
						map.put(type, list);
					}
					list.add(instance);
				}
			}
		}
	}
	
	private List<ActionInterceptor> getInterceptors(Class<?> type){
		ArrayList<ActionInterceptor> out = new ArrayList<ActionInterceptor>();
		while(!type.equals(AtomicAction.class)){
			List<ActionInterceptor> list = map.get(type);
			if(list != null){
				out.addAll(list);
			}
			type = type.getSuperclass();
		}
		return out;
	}
	
	@SuppressWarnings("rawtypes")
	public void process(AtomicAction action, Object result){
		List<ActionInterceptor> interceptors = getInterceptors(action.getClass());
		for(ActionInterceptor interceptor : interceptors){
			try {
				interceptor.afterInvoke(action, result);
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

}
