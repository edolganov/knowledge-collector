package ru.chapaj.util.event.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ru.chapaj.util.event.Event;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LastEventListener {
	
	Class<? extends Event<?>> value();

}
