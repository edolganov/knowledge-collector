package ru.kc.platform.aop;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.platform.annotations.Inject;
import ru.kc.platform.app.AppContext;

public class AOPTool {
	
	private static final Log log = LogFactory.getLog(AOPTool.class);
	
	AppContext context;

	public AOPTool(AppContext context) {
		super();
		this.context = context;
	}
	
	public void injectData(Object ob){
		if(context.dataForInject.size() == 0) return;
		
		List<Field> requiredField = getRequiredField(ob);
		if(requiredField.size() == 0) return;
		for (Field field : requiredField) {
			Object objectToInject = findObjectToInject(field);
			if(objectToInject == null){
				log.warn("can't find object to inject by "+field+" for "+ob);
				continue;
			} else {
				try {
					inject(field, ob, objectToInject);
				}catch (Exception e) {
					log.error("can't set value to "+field+" for "+ob);
				}

			}
		}
		
	}
	
	private List<Field> getRequiredField(Object c) {
		ArrayList<Field> out = new ArrayList<Field>();
		Class<?> curClass = c.getClass();
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
	
	private Object findObjectToInject(Field field) {
		Class<?> declaringType = field.getType();
		for (Object candidat : context.dataForInject) {
			if(candidat.getClass().equals(declaringType)){
				return candidat;
			}
		}
		return null;
	}


	private void inject(Field field, Object ob, Object objectToInject) throws Exception {
		field.setAccessible(true);
		field.set(ob, objectToInject);
	}

}
