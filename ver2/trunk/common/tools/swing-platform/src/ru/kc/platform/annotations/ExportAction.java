package ru.kc.platform.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExportAction {
	
    String mapping() default "";
    
    String icon() default "";
    
    String description() default "";
    
    

}
