package ru.dolganov.tool.knowledge.collector.main;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import ru.chapaj.util.swing.IconHelper;
import ru.dolganov.tool.knowledge.collector.App;
import ru.dolganov.tool.knowledge.collector.AppListener;
import ru.dolganov.tool.knowledge.collector.Controller;
import ru.dolganov.tool.knowledge.collector.annotation.ControllerInfo;

@ControllerInfo(target=MainWindow.class)
public class ShowHideInfoController extends Controller<MainWindow> {
	
	MainWindow ui;
	ImageIcon leftIcon = IconHelper.get("/images/kc/app/left.png");
	ImageIcon rightIcon = IconHelper.get("/images/kc/app/right.png");

	@Override
	public void init(final MainWindow ui) {
		this.ui = ui;
		ui.showHideInfoB.setText("");

		ui.showHideInfoB.setIcon(rightIcon);
		ui.showHideInfoB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				showHideInfo();
			}
		});
		
		
		App.getDefault().addListener(new AppListener(){

			@Override
			public void onAction(Object source, String action, Object... data) {				
				if("need-show-hide-info-action".equals(action)){
					showHideInfo();
				}
			}
			
		});
	}
	
	private void showHideInfo(){
		if(ui.infoPanel.isVisible()){
			ui.infoPanel.setVisible(false);
			Rectangle infoBounds = ui.infoPanel.getBounds();
			Rectangle treeBounds = ui.treePanel.getBounds();
			int wi = infoBounds.width;
			int wt = treeBounds.width;
			ui.treePanel.setBounds(
					treeBounds.x, 
					treeBounds.y, 
					wt + wi, 
					treeBounds.height);
			ui.treePanel.repaint();
			ui.showHideInfoB.setIcon(leftIcon);
			App.getDefault().fireAction(this, "hide-info");
		}
		else {
			Rectangle infoBounds = ui.infoPanel.getBounds();
			Rectangle treeBounds = ui.treePanel.getBounds();
			int wi = infoBounds.width;
			int wt = treeBounds.width;
			ui.treePanel.setBounds(
					treeBounds.x, 
					treeBounds.y, 
					wt - wi, 
					treeBounds.height);
			ui.treePanel.repaint();
			ui.showHideInfoB.setIcon(rightIcon);
			ui.infoPanel.setVisible(true);
			App.getDefault().fireAction(this, "show-info");
		}
	}
	

}
