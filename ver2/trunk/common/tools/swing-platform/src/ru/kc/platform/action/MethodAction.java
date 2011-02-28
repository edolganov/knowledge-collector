package ru.kc.platform.action;

import java.lang.reflect.Method;

import javax.swing.Icon;

import ru.kc.platform.annotations.ExportAction;
import ru.kc.platform.controller.AbstractController;
import ru.kc.util.swing.icon.IconUtil;

public class MethodAction extends AbstractAction {
	
	private final Method method;
	private final ExportAction data;
	private final AbstractController<?> c;
	
	public MethodAction(Method method, ExportAction data, AbstractController<?> c) {
		super();
		this.method = method;
		this.data = data;
		this.c = c;
	}

	protected String getText() {
		return data.description();
	}
	
	protected Icon getIcon() {
		String icon = data.icon();
		if("".equals(icon)) return null;
		return IconUtil.get(icon);
	}
	
	@Override
	protected void actionPerformed() throws Exception {
		method.invoke(c);
	}


	@Override
	public String toString() {
		return "MethodAction [c=" + c + ", method=" + method + ", data=" + data
				+ "]";
	}


	

	
}
