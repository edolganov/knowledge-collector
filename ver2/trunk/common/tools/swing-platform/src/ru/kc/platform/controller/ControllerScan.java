package ru.kc.platform.controller;

import java.lang.reflect.Method;
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

import ru.kc.platform.action.MethodAction;
import ru.kc.platform.annotations.Dependence;
import ru.kc.platform.annotations.ExportAction;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.aop.AOPTool;
import ru.kc.platform.app.AppContext;
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
	

	public List<AbstractController<?>> scanAndInit(String packagePreffix, Object initOb){
		return scanAndInit(packagePreffix, initOb, null);
	}

	@SuppressWarnings({ "rawtypes" })
	public List<AbstractController<?>> scanAndInit(String packagePreffix, Object initOb, Class<?>[] blackList){
		
		ArrayList<AbstractController<?>> out = new ArrayList<AbstractController<?>>();
		
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
			
			if(!AbstractController.class.isAssignableFrom(controller)){
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
					if(!AbstractController.class.isAssignableFrom(dependenceClass)){
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
				AbstractController c = (AbstractController<?>) node.controllerClass.newInstance();

				init(c,initOb);
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


	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void init(AbstractController c,Object initOb) {
		injectData(c);
		injectMethodActions(c);
		c.init(appContext, initOb);
	}


	private void injectData(AbstractController<?> c) {
		new AOPTool(appContext).injectData(c);
	}
	

	private void injectMethodActions(AbstractController<?> c) {
		List<MethodAction> actions = findMethodActions(c);
		c.setMethodActions(actions);
	}


	private List<MethodAction> findMethodActions(AbstractController<?> c) {
		ArrayList<MethodAction> out = new ArrayList<MethodAction>();
		Class<?> curClass = c.getClass();
		while(!curClass.equals(AbstractController.class)){
			Method[] methods = curClass.getDeclaredMethods();
			for(Method candidat : methods){
				ExportAction annotation = candidat.getAnnotation(ExportAction.class);
				if(annotation != null){
					out.add(new MethodAction(candidat, annotation, c));
				}
			}
			curClass = curClass.getSuperclass();
		}
		return out;
	}





	

}
