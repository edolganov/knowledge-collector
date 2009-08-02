package ru.chapaj.tool.link.collector;

import java.awt.Image;
import java.awt.Toolkit;

import ru.chapaj.util.log.Log;

public class ImageBundle {
	private static Log log = Log.getInstance(ImageBundle.class);
	
	private static ImageBundle def;
	
	public static synchronized ImageBundle getDefault(){
		if(def == null) def = new ImageBundle();
		return def;
	}
	
	Image appIcon;
	


	public ImageBundle() {
		Toolkit t = Toolkit.getDefaultToolkit();
		try {
			appIcon = t.getImage(this.getClass().getResource("/images/lc-icon.png"));
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	public Image getAppIcon() {
		return appIcon;
	}

}
