package ru.kc.util.swing.icon;

import java.io.File;
import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;

public class IconUtil {
	
	static final HashMap<String, ImageIcon> cache = new HashMap<String, ImageIcon>();
	
	public static synchronized ImageIcon get(String path){
		ImageIcon icon = cache.get(path);
		if(icon == null){
			URL resource = IconUtil.class.getResource(path);
			if(resource == null){
				resource = fromFile(path);
			}
			
			if(resource == null){
				//System.err.println("no resourse by path "+path);
				icon = unknowIcon();
			} else {
				try {
					icon = new ImageIcon(resource);
					cache.put(path, icon);
				}catch (Exception e) {
					e.printStackTrace();
					icon = unknowIcon();
				}
			}
		}
		return new ImageIcon(icon.getImage());
	}
	
	private static URL fromFile(String path) {
		URL resource = null;
		try {
			File file = new File(path);
			if(file.exists()){
				resource = file.toURI().toURL();
			}
		}catch (Exception e) {
			e.printStackTrace();
			resource = null;
		}
		
		return resource;
	}

	private static ImageIcon unknowIcon(){
		URL resource = ImageIcon.class.getResource("/ru/kc/util/swing/icon/unknown.png");
		return new ImageIcon(resource);
	}

}
