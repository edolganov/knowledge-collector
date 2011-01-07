package ru.dolganov.tool.knowledge.collector.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Флаг, маркирующий контроллер.
 * Используется при автоматической инициализации контроллеров
 * @author jenua.dolganov
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ControllerInfo {
	
	/**
	 * цель для контроллера
	 */
	Class<?> target();
	
	Class<?> dependence() default Object.class;
}
