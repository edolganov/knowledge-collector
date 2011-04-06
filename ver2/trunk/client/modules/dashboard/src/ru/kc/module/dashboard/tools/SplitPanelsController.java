package ru.kc.module.dashboard.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JSplitPane;

import ru.kc.common.controller.Controller;
import ru.kc.module.dashboard.ui.Dashboard;
import ru.kc.platform.action.facade.ButtonFacade;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.swing.icon.IconUtil;

@Mapping(Dashboard.class)
public class SplitPanelsController extends Controller<Dashboard>{
	
	private static final String COMMON_GROUP = "3-common";
	
	ButtonFacade showHideProps;
	JSplitPane leftSplitPane ;
	ImageIcon leftIcon = IconUtil.get("/ru/kc/common/img/left.png");
	ImageIcon rightIcon = IconUtil.get("/ru/kc/common/img/right.png");
	
	@Override
	public void init() {
		
		leftSplitPane = ui.jSplitPane2;
		leftSplitPane.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				String propertyName = evt.getPropertyName();
				System.out.println(propertyName);
				if("lastDividerLocation".equals(propertyName)){
					Integer newValue = (Integer)evt.getNewValue();
					System.out.println(leftSplitPane.getWidth()+" "+newValue);
					
				}
			}
		});
		
		showHideProps = actionService.addButtonAction();
		showHideProps.setIcon(rightIcon);
		showHideProps.setToolTipText("Create dir");
		showHideProps.setGroup(COMMON_GROUP);
		showHideProps.addListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//invokeSafe(new CreateDirRequest(parent));
			}
		});
	}

}
