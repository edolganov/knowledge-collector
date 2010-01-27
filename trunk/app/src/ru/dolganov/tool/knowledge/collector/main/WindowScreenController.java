package ru.dolganov.tool.knowledge.collector.main;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ru.dolganov.tool.knowledge.collector.Controller;
import ru.dolganov.tool.knowledge.collector.annotation.ControllerInfo;

@ControllerInfo(target=MainWindow.class)
public class WindowScreenController extends Controller<MainWindow> {

	
	int oldW;
	int oldH;
	int oldPX;
	int oldPY;
	
	boolean fullScreen = false;

	@Override
	public void init(final MainWindow ui_) {
		
		ui_.fullStandart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				if(!fullScreen){
					Dimension oldSize = ui_.getSize();
					oldPX = ui_.getX();
					oldPY = ui_.getY();
					
					oldW = oldSize.width;
					oldH = oldSize.height;
					
//					GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//					GraphicsDevice gs = ge.getDefaultScreenDevice();
//					DisplayMode displayMode = gs.getDisplayMode();
//					int height = displayMode.getHeight();
//					int width = displayMode.getWidth();
					
//					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//					int height = screenSize.height;
//					int width = screenSize.width;
					
					//get local graphics environment
					GraphicsEnvironment graphicsEnvironment=GraphicsEnvironment.getLocalGraphicsEnvironment();
					//get maximum window bounds
					Rectangle maximumWindowBounds=graphicsEnvironment.getMaximumWindowBounds();
					
					int height = maximumWindowBounds.height;
					int width = maximumWindowBounds.width;
					
					Dimension minimumSize = ui_.getMinimumSize();
					ui_.setBounds(0, 0, Math.max(width, minimumSize.width), Math.max(height, minimumSize.height));
					

					
					
					
					fullScreen = true;
				}
				else{
					
					ui_.setBounds(oldPX, oldPY, oldW, oldH);
					
					fullScreen = false;
				}
				
			}
		});
		
	}
	

}
