package ru.kc.main.tools;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import ru.kc.common.controller.Controller;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.platform.profile.Profile;
import ru.kc.platform.profile.event.ProfileLoaded;
import ru.kc.platform.profile.event.ProfilePersisting;
import ru.kc.platform.ui.tabbedform.MainForm;

@Mapping(MainForm.class)
public class WindowScreenController extends Controller<MainForm> {

	
	private int oldW;
	private int oldH;
	private int oldPX;
	private int oldPY;
	private boolean fullScreen = false;

	@Override
	public void init() {
		
		Dimension oldSize = ui.getSize();
		oldW = oldSize.width;
		oldH = oldSize.height;
		oldPX = ui.getX();
		oldPY = ui.getY();
	}
	
	@EventListener
	public void onProfileLoaded(ProfileLoaded event){
		loadPosition(event.profile);
	}
	
	@EventListener
	public void onProfilePersisting(ProfilePersisting event){
		savePosition(event.profile);
	}
	
	private void loadPosition(Profile profile) {
		String width = profile.get("ui-width");
		String height = profile.get("ui-height");
		String x = profile.get("ui-x");
		String y = profile.get("ui-y");
		
		int oldWValue;
		int oldHValue;
		int oldPXValue;
		int oldPYValue;
		try {
			oldWValue = Integer.parseInt(width);
			oldHValue = Integer.parseInt(height);
			oldPXValue = Integer.parseInt(x);
			oldPYValue =Integer.parseInt(y);
		}catch (Exception e) {
			return;
		}
		
		oldW = oldWValue;
		oldH = oldHValue;
		oldPX = oldPXValue;
		oldPY = oldPYValue;
		
		String fullscreen = profile.get("ui-fullscreen");
		if("true".equals(fullscreen)) fullScreen = true;
		
		if(fullScreen){
			setFullScreen();
		} else {
			setBound();
		}
	}
	
	private void setFullScreen() {
		//get local graphics environment
		GraphicsEnvironment graphicsEnvironment=GraphicsEnvironment.getLocalGraphicsEnvironment();
		//get maximum window bounds
		Rectangle maximumWindowBounds=graphicsEnvironment.getMaximumWindowBounds();
		
		int height = maximumWindowBounds.height;
		int width = maximumWindowBounds.width;
		
		Dimension minimumSize = ui.getMinimumSize();
		ui.setBounds(0, 0, Math.max(width, minimumSize.width), Math.max(height, minimumSize.height));
	}
	
	private void setBound() {
		ui.setBounds(oldPX, oldPY, oldW, oldH);
	}


	
	private void savePosition(Profile profile) {
		if(!fullScreen){
			profile.put("ui-width",""+ui.getWidth());
			profile.put("ui-height",""+ui.getHeight());
			profile.put("ui-x",""+ui.getX());
			profile.put("ui-y",""+ui.getY());
		}
		else {
			profile.put("ui-width",""+oldW);
			profile.put("ui-height",""+oldH);
			profile.put("ui-x",""+oldPX);
			profile.put("ui-y",""+oldPY);
		}
		
		profile.put("ui-fullscreen",""+fullScreen);
	}
	

}
