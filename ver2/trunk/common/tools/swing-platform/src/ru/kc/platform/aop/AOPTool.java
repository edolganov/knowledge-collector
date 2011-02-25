package ru.kc.platform.aop;

import java.awt.Component;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.platform.annotations.Inject;
import ru.kc.platform.annotations.OptionalInject;
import ru.kc.platform.app.AppContext;

public class AOPTool {
	
	private static final Log log = LogFactory.getLog(AOPTool.class);
	
	AppContext context;
	HashMap<Class<?>, Object> servicesToInject = new HashMap<Class<?>, Object>();

	public AOPTool(AppContext context, Object uiObject) {
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
		injectDataFromUITree(ob);
	}

	private void injectDataFromContext(Object ob) {
		
		List<Field> requiredField = getRequiredFields(ob);
		for (Field field : requiredField) {
			Object objectToInject = findObjectToInject(field);
			if(objectToInject == null){
				log.warn("can't find object to inject by "+field+" for "+ob);
				continue;
			} else {
				try {
					inject(field, ob, objectToInject);
				}catch (Exception e) {
					log.error("can't set value for "+field);
				}

			}
		}
	}
	
	private void injectDataFromUITree(Object ob) {
		
		List<Field> optionalField = getOptionalFields(ob);
		for (Field field : optionalField) {
			Object serviceToInject = findServiceToInject(field);
			if(serviceToInject == null){
				log.warn("can't find object to inject for "+field);
				continue;
			} else {
				try {
					inject(field, ob, serviceToInject);
				}catch (Exception e) {
					log.error("can't set value to "+field+" for "+ob);
				}

			}
		}
	}
	

	private List<Field> getRequiredFields(Object ob) {
		ArrayList<Field> out = new ArrayList<Field>();
		Class<?> curClass = ob.getClass();
		while(!curClass.equals(Object.class)){
			Field[] fields = curClass.getDeclaredFields();
			for(Field candidat : fields){
				Inject inject = candidat.getAnnotation(Inject.class);
				if(inject != null){
					out.add(candidat);
				}
			}
			curClass = curClass.getSuperclass();
		}
		return out;
	}
	

	private List<Field> getOptionalFields(Object ob) {
		ArrayList<Field> out = new ArrayList<Field>();
		Class<?> curClass = ob.getClass();
		while(!curClass.equals(Object.class)){
			Field[] fields = curClass.getDeclaredFields();
			for(Field candidat : fields){
				OptionalInject inject = candidat.getAnnotation(OptionalInject.class);
				if(inject != null){
					out.add(candidat);
				}
			}
			curClass = curClass.getSuperclass();
		}
		return out;
	}
	
	private Object findObjectToInject(Field field) {
		Class<?> declaringType = field.getType();
		for (Object candidat : context.dataForInject) {
			if(candidat.getClass().equals(declaringType)){
				return candidat;
			}
		}
		return null;
	}
	
	private Object findServiceToInject(Field field) {
		Class<?> declaringType = field.getType();
		for (Class<?> candidatType : servicesToInject.keySet()) {
			if(candidatType.equals(declaringType)){
				return servicesToInject.get(candidatType);
			}
		}
		return null;
	}


	private void inject(Field field, Object ob, Object objectToInject) throws Exception {
		field.setAccessible(true);
		field.set(ob, objectToInject);
	}

}
