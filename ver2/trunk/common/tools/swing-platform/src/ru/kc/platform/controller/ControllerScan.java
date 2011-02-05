package ru.kc.platform.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import ru.kc.platform.app.AppContext;
import ru.kc.platform.controller.annotations.Dependence;
import ru.kc.platform.controller.annotations.Mapping;
import ru.kc.util.Check;

public class ControllerScan {
	
	private static final Log log = LogFactory.getLog(ControllerScan.class);
	
	private static class ControllerDependenceNode {
		Class<?> controllerClass;
		ArrayList<ControllerDependenceNode> children;
		
		public ControllerDependenceNode(Class<?> controllerClass) {
			super();
			this.controllerClass = controllerClass;
		}

		@Override
		public String toString() {
			return "ControllerDependenceNode [controllerClass=" + controllerClass + "]";
		}
	}
		
	private AppContext appContext;

	
	
	public ControllerScan(AppContext appContext) {
		this.appContext = appContext;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Controller<?>> scanAndInit(String packagePreffix, Object initOb, Class<?>[] blackList){
		
		ArrayList<Controller<?>> out = new ArrayList<Controller<?>>();
		
		if(packagePreffix == null) 
			throw new IllegalArgumentException("packagePreffix is null");
		if(initOb == null) 
			throw new IllegalArgumentException("initOb is null");
		
		Class<?> initObClazz = initOb.getClass();
		if(blackList == null) blackList = new Class[0];
		
		ArrayList<ControllerDependenceNode> firstLevelNodes = new ArrayList<ControllerDependenceNode>();
		HashMap<Class<?>, ControllerDependenceNode> allNodes = new HashMap<Class<?>, ControllerScan.ControllerDependenceNode>();
		HashMap<Class<?>, ArrayList<ControllerDependenceNode>> dependenceNodes = new HashMap<Class<?>, ArrayList<ControllerDependenceNode>>();
		
		Reflections reflections = new Reflections(new ConfigurationBuilder()
        .setUrls(ClasspathHelper.getUrlsForPackagePrefix(packagePreffix))
        .setScanners(new TypeAnnotationsScanner())
		.filterInputsBy(new FilterBuilder.Include(FilterBuilder.prefix(packagePreffix))));
		
		Set<Class<?>> all = reflections.getTypesAnnotatedWith(Mapping.class);
		//log.info("found: "+all.size());
		
		for (Class<?> controller : all) {
			if(Check.contains(controller,blackList)){
				log.info("skip controller from black list: "+controller);
				continue;
			}
			
			if(!Controller.class.isAssignableFrom(controller)){
				log.error("invalid controller's class:"+controller.getName());
			}
			
			Mapping ci = controller.getAnnotation(Mapping.class);
			try {
				Class<?> targetClass = ci.value();
				if(targetClass == null) throw new IllegalStateException("null target of controller:"+controller);
				
				if(!targetClass.equals(initObClazz)){
					continue;
				}
				
				ControllerDependenceNode node = new ControllerDependenceNode(controller);
				allNodes.put(controller, node);
				
				//инитим зависимости
				Dependence dependenceAnnotation = controller.getAnnotation(Dependence.class);
				Class<?> dependenceClass =  dependenceAnnotation != null? dependenceAnnotation.value() : null;
				if(dependenceClass == null){
					firstLevelNodes.add(node);
				} else {
					if(!Controller.class.isAssignableFrom(dependenceClass)){
						log.error("invalid controller's dependence class:"+dependenceClass.getName());
					} else {
						ArrayList<ControllerDependenceNode> list = dependenceNodes.get(dependenceClass);
						if(list == null){
							list = new ArrayList<ControllerScan.ControllerDependenceNode>();
							dependenceNodes.put(dependenceClass, list);
						}
						list.add(node);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//строим дерево зависимостей
		for(Class<?> key : dependenceNodes.keySet()){
			ArrayList<ControllerDependenceNode> children = dependenceNodes.get(key);
			ControllerDependenceNode node = allNodes.get(key);
			if(node == null){
				log.error("can't find controller class "+key.getName() + " for "+children);
			} else {
				node.children = children;
			}
		}
		
		//инитим дерево начиная с рута
		LinkedList<ControllerDependenceNode> queue = new LinkedList<ControllerScan.ControllerDependenceNode>(firstLevelNodes);
		while(!queue.isEmpty()){
			ControllerDependenceNode node = queue.removeFirst();
			try {
				Controller c = (Controller<?>) node.controllerClass.newInstance();

				//log.info("init controller: "+c.getClass().getName());
				c.init(appContext, initOb);
				out.add(c);
				
				allNodes.remove(node.controllerClass);
				
				if(node.children != null){
					for(ControllerDependenceNode child : node.children){
						queue.addLast(child);
					}
				}
			}catch (Exception e) {
				log.error(e);
			}
		}
		
		//проверяем что не осталось незаиниченных контроллеров
		for (Class<?> controller : allNodes.keySet()) {
			log.error("find not inited controller class "+controller.getName());
		}
		
		firstLevelNodes.clear();
		allNodes.clear();
		dependenceNodes.clear();
		
		return out;
	}
	

}