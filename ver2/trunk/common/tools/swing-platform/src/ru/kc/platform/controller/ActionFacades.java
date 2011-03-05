package ru.kc.platform.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ru.kc.platform.action.facade.AbstractActionFacade;
import ru.kc.platform.action.facade.ActionService;
import ru.kc.platform.action.facade.ButtonFacade;
import ru.kc.platform.action.facade.ButtonFacadeMediator;
import ru.kc.platform.action.facade.ComboBoxFacade;
import ru.kc.platform.action.facade.ComboBoxFacadeMediator;
import ru.kc.platform.annotations.ExportAction;
import ru.kc.util.swing.icon.IconUtil;

public class ActionFacades implements ActionService {
	
	private AbstractController<?> controller;
	private List<AbstractActionFacade> actionFacades = new ArrayList<AbstractActionFacade>();
	
	
	public ActionFacades(AbstractController<?> controller) {
		super();
		this.controller = controller;
	}
	
	public void init(){
		initActionFacades();
	}
	
	private void initActionFacades() {
		Class<?> curClass = controller.getClass();
		while(!curClass.equals(AbstractController.class)){
			Method[] methods = curClass.getDeclaredMethods();
			for(Method candidat : methods){
				ExportAction annotation = candidat.getAnnotation(ExportAction.class);
				if(annotation != null){
					actionFacades.add(createButtonFacadeMediator(annotation, candidat));
				}
			}
			curClass = curClass.getSuperclass();
		}
	}
	
	
	private AbstractActionFacade createButtonFacadeMediator(ExportAction annotation, final Method method) {
		ButtonFacadeMediator mediator = new ButtonFacadeMediator();
		mediator.setIcon(IconUtil.get(annotation.icon()));
		mediator.setToolTipText(annotation.description());
		mediator.addListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try{
					method.invoke(controller);
				}catch (Exception ex) {
					controller.log.error("invoke error for "+method,ex);
				}
				
			}
		});
		return mediator;
	}
	
	public List<AbstractActionFacade> getAll(){
		return actionFacades;
	}

	@Override
	public ButtonFacade addButtonAction() {
		ButtonFacadeMediator mediator = new ButtonFacadeMediator();
		actionFacades.add(mediator);
		return mediator;
	}

	@Override
	public ComboBoxFacade addComboBoxAction() {
		ComboBoxFacadeMediator mediator = new ComboBoxFacadeMediator();
		actionFacades.add(mediator);
		return mediator;
	}
	

}
