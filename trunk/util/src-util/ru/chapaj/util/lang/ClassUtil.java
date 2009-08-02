package ru.chapaj.util.lang;

public class ClassUtil {
	
	public static boolean isValid(Class<?> candidat, Class<?> valid){
		return candidat.equals(valid) || candidat.isInstance(valid);
	}

}
