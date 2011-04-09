package ru.kc.util.reflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ru.kc.util.annotation.Inject;

public class ReflectionUtil {


	public static void injectDataFromContext(Object ob, List<Object> context) throws Exception {
		
		List<Field> requiredField = getRequiredFields(ob, context);
		for (Field field : requiredField) {
			Object objectToInject = findObjectToInject(field, context);
			if(objectToInject == null){
				throw new IllegalStateException("can't find object to inject by "+field+" for "+ob);
			} else {
				try {
					inject(field, ob, objectToInject);
				}catch (Exception e) {
					throw new IllegalStateException("can't set value for "+field);
				}

			}
		}
	}
	

	private static List<Field> getRequiredFields(Object ob, List<Object> context) {
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
	
	private static Object findObjectToInject(Field field, List<Object> context) {
		Class<?> declaringType = field.getType();
		for (Object candidat : context) {
			if(candidat.getClass().equals(declaringType)){
				return candidat;
			}
		}
		return null;
	}


	private static void inject(Field field, Object ob, Object objectToInject) throws Exception {
		field.setAccessible(true);
		field.set(ob, objectToInject);
	}

}
