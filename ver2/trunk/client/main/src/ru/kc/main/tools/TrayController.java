package ru.kc.main.tools;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import ru.kc.common.controller.Controller;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.common.event.CloseRequest;
import ru.kc.platform.ui.tabbedform.MainForm;
import ru.kc.util.swing.icon.IconUtil;

@Mapping(MainForm.class)
public class TrayController extends Controller<MainForm> {

	@Override
	protected void init() {}
	
	@Override
	protected void afterAllInited() {
		final TrayIcon trayIcon;

		if (SystemTray.isSupported()) {

		    SystemTray tray = SystemTray.getSystemTray();

		    MouseListener mouseListener = new MouseAdapter() {
		                
		        public void mouseClicked(MouseEvent e) {
		        	if(e.getButton() == 1){
		        		ui.setVisible(true);
		        		ui.toFront();
		        	}
		        }
		        
		    };

		    ActionListener exitListener = new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		            invokeSafe(new CloseRequest());
		        }
		    };
		            
		    PopupMenu popup = new PopupMenu();
		    MenuItem defaultItem = new MenuItem("Exit");
		    defaultItem.addActionListener(exitListener);
		    popup.add(defaultItem);

		    List<Image> iconImages = ui.getIconImages();
		    if(iconImages.size() == 0) {
		    	iconImages = new ArrayList<Image>();
		    	iconImages.add(IconUtil.get("/ru/kc/common/img/app.png").getImage());
		    }
			trayIcon = new TrayIcon(iconImages.get(0), ui.getTitle(), popup);
		    trayIcon.setImageAutoSize(true);
		    trayIcon.addMouseListener(mouseListener);

		    try {
		        tray.add(trayIcon);
		    } catch (AWTException e) {
		        log.error("TrayIcon could not be added.", e);
		    }

		} else {
		    //  System Tray is not supported
		}
	}

}
