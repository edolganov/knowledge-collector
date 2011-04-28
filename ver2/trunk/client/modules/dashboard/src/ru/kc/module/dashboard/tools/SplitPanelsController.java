package ru.kc.module.dashboard.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JSplitPane;

import ru.kc.common.controller.Controller;
import ru.kc.common.dashboard.event.OpenHideRightPanel;
import ru.kc.module.dashboard.ui.Dashboard;
import ru.kc.platform.action.facade.AbstractButtonFacade;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.util.swing.icon.IconUtil;

@Mapping(Dashboard.class)
public class SplitPanelsController extends Controller<Dashboard>{
	
	private static final String COMMON_GROUP = "3-common";
	
	AbstractButtonFacade showHideLeft;
	AbstractButtonFacade showHideRight;
	
	JSplitPane leftSplitPane;
	JSplitPane rightSplitPane;
	
	
	@Override
	public void init() {
		
		leftSplitPane = ui.jSplitPane1;
		leftSplitPane.setContinuousLayout(true);

		rightSplitPane = ui.jSplitPane2;
		rightSplitPane.setContinuousLayout(true);
		rightSplitPane.setResizeWeight(1d);
		
		showHideLeft = actionService.addButtonAction();
		showHideLeft.setIcon(IconUtil.get("/ru/kc/common/img/left_panel.png"));
		showHideLeft.setToolTipText("Show/hide left panel");
		showHideLeft.setGroup(COMMON_GROUP);
		showHideLeft.addListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showHide(leftSplitPane, true);
			}
		});
		
		showHideRight = actionService.addButtonAction();
		showHideRight.setIcon(IconUtil.get("/ru/kc/common/img/right_panel.png"));
		showHideRight.setToolTipText("Show/hide right panel  (Ctrl+H)");
		showHideRight.setGroup(COMMON_GROUP);
		showHideRight.addListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showHide(rightSplitPane, false);
			}
		});
	}
	
	@EventListener
	public void onOpenHideRightPanel(OpenHideRightPanel event){
		showHide(rightSplitPane, false);
	}
	
	private void showHide(JSplitPane pane, boolean left){
		int cur = pane.getDividerLocation();
		int last = pane.getLastDividerLocation();
		if(left){
			if(cur != pane.getMinimumDividerLocation()) {
				pane.setDividerLocation(pane.getMinimumDividerLocation());
			} else {
				pane.setDividerLocation(last);
			}
		} else {
			int max = pane.getMaximumDividerLocation();
			if(cur != max) {
				pane.setDividerLocation(max);
			} else {
				if(last == max){
					last = max - 300;
				} else if(last < 0){
					last = pane.getWidth() - 300;
				}
				pane.setDividerLocation(last);
			}
		}

	}

}
