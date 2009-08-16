package ru.chapaj.util.swing;

import javax.swing.ImageIcon;

public class IconHelper {
	
	public static ImageIcon get(String path){
		return new ImageIcon(ImageIcon.class.getResource(path));
	}

}
