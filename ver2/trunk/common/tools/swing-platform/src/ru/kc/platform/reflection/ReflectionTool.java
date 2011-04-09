package ru.kc.platform.reflection;

import java.awt.Component;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.platform.app.AppContext;
import ru.kc.util.reflection.ReflectionUtil;

public class ReflectionTool {
	
	private static final Log log = LogFactory.getLog(ReflectionTool.class);
	
	AppContext context;
	HashMap<Class<?>, Object> servicesToInject = new HashMap<Class<?>, Object>();

	public ReflectionTool(AppContext context, Object uiObject) {
		super();
		this.context = context;
		findServicesToInject(uiObject);
	}
	
	private void findServicesToInject(Object uiObject) {
		if(uiObject instanceof Component){
			
		} else throw new IllegalStateException("unknow type of resource object: "+uiObject);
	}

	public void injectData(Object ob){
		injectDataFromContext(ob);
	}

	private void injectDataFromContext(Object ob) {
		try {
			ReflectionUtil.injectDataFromContext(ob, context.dataForInject);
		}catch (Exception e) {
			log.error("", e);
		}
	}

}
