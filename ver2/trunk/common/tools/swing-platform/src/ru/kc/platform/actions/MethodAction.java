package ru.kc.platform.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

import javax.swing.Icon;
import javax.swing.JButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.platform.annotations.ExportAction;
import ru.kc.platform.controller.AbstractController;
import ru.kc.util.swing.icon.IconUtil;

public class MethodAction {
	
	private static final Log log = LogFactory.getLog(MethodAction.class);
	
	private final Method method;
	private final ExportAction data;
	private final AbstractController<?> c;
	
	public MethodAction(Method method, ExportAction data, AbstractController<?> c) {
		super();
		this.method = method;
		this.data = data;
		this.c = c;
	}
	
	
	public JButton createButton(){
		return createButton(false);
	}
	
	public JButton createButton(boolean textInToolTip){
		JButton button = new JButton();
		if(!textInToolTip){
			button.setText(getText());
		} else {
			button.setToolTipText(getText());
		}
		button.setIcon(getIcon());
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					method.invoke(c);
				}catch (Exception ex) {
					log.error(e);
				}
			}
		});
		return button;
	}




	private String getText() {
		return data.description();
	}
	
	private Icon getIcon() {
		String icon = data.icon();
		if("".equals(icon)) return null;
		return IconUtil.get(icon);
	}


	@Override
	public String toString() {
		return "MethodAction [c=" + c + ", method=" + method + ", data=" + data
				+ "]";
	}
	

	
}
