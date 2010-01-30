package ru.chapaj.util.swing;

import java.util.HashMap;

import javax.swing.ImageIcon;

public class IconHelper {
	
	static final HashMap<String, ImageIcon> cache = new HashMap<String, ImageIcon>();
	
	public static ImageIcon get(String path){
		ImageIcon icon = cache.get(path);
		if(icon == null){
			icon = new ImageIcon(ImageIcon.class.getResource(path));
			cache.put(path, icon);
		}
		return new ImageIcon(icon.getImage());
	}

}
