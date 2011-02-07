package ru.kc.platform.actions;

import java.lang.reflect.Method;

import ru.kc.platform.annotations.ExportAction;
import ru.kc.platform.controller.AbstractController;

public class MethodAction {
	
	public final Method method;
	public final ExportAction data;
	public final AbstractController<?> c;
	
	public MethodAction(Method method, ExportAction data, AbstractController<?> c) {
		super();
		this.method = method;
		this.data = data;
		this.c = c;
	}

	@Override
	public String toString() {
		return "MethodAction [c=" + c + ", method=" + method + ", data=" + data
				+ "]";
	}
	
}
