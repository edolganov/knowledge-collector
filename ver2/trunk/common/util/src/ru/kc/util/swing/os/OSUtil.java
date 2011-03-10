package ru.kc.util.swing.os;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class OSUtil {
	
	public static void openUrl(String url) throws IOException, URISyntaxException{
		Desktop desktop = Desktop.getDesktop();
		desktop.browse(new URI(url));
	}

	public static void openFile(String url) throws IOException {
		String osName = System.getProperty("os.name");
		if (osName.startsWith("Windows")){
			Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
		} else {
			Desktop desktop = Desktop.getDesktop();
			desktop.open(new File(url));
		}
		
	}
}
