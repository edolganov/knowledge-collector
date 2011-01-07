package ru.chapaj.tool.link.collector.controller;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import ru.chapaj.tool.link.collector.App;
import ru.chapaj.tool.link.collector.ImageBundle;
import ru.chapaj.tool.link.collector.ui.AppFrame;

public class IconController extends Controller<AppFrame> {

	@Override
	public void init(AppFrame ui) {
		final TrayIcon trayIcon;

		if (SystemTray.isSupported()) {

		    SystemTray tray = SystemTray.getSystemTray();

		    MouseListener mouseListener = new MouseAdapter() {
		                
		        public void mouseClicked(MouseEvent e) {
		        	if(e.getButton() == 1){
		        		App.getDefault().show();
		        	}
		        }
		        
		    };

		    ActionListener exitListener = new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		            App.getDefault().exit();
		        }
		    };
		            
		    PopupMenu popup = new PopupMenu();
		    MenuItem defaultItem = new MenuItem("Exit");
		    defaultItem.addActionListener(exitListener);
		    popup.add(defaultItem);

		    trayIcon = new TrayIcon(ImageBundle.getDefault().getAppIcon(), "Link Collector", popup);

//		    ActionListener actionListener = new ActionListener() {
//		        public void actionPerformed(ActionEvent e) {
//		            trayIcon.displayMessage("Action Event", 
//		                "An Action Event Has Been Performed!",
//		                TrayIcon.MessageType.INFO);
//		        }
//		    };
		            
		    trayIcon.setImageAutoSize(true);
//		    trayIcon.addActionListener(actionListener);
		    trayIcon.addMouseListener(mouseListener);

		    try {
		        tray.add(trayIcon);
		    } catch (AWTException e) {
		        System.err.println("TrayIcon could not be added.");
		    }

		} else {

		    //  System Tray is not supported

		}

	}

}
