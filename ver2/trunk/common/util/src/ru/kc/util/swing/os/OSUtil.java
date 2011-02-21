package ru.kc.util.swing.os;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class OSUtil {
	
	public static void openUrl(String url) throws IOException, URISyntaxException{
		Desktop desktop = Desktop.getDesktop();
		desktop.browse(new URI(url));
	}

}
