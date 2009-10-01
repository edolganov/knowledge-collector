package ru.chapaj.util.lang;

public class ClassUtil {
	
	public static boolean isValid(Class<?> candidat, Class<?> valid){
		boolean equals = candidat.equals(valid);
		if(equals) return true;
		boolean instance = candidat.isInstance(valid);
		if(instance) return true;
		return valid.isAssignableFrom(candidat);
	}

}
