package ru.chapaj.tool.link.collector.command;

import ru.chapaj.tool.link.collector.model.Link;
import ru.chapaj.util.log.Log;
import ru.chapaj.util.os.BareBonesBrowserLaunch;
import ru.chapaj.util.os.ProcessWrapper;
import ru.chapaj.util.os.win.WinUtil;

public class OpenLinkByDefault extends Command {
	
	Link link;
	
	public OpenLinkByDefault(Link link) {
		this.link = link;
	}

	@Override
	public <T> T invoke() {
		try {
			String query = link.getUrl();
			//it's url
			if(query.startsWith("http") || query.startsWith("www")){
				BareBonesBrowserLaunch.openURL(link.getUrl());
				return null;
			}
			
			//it's local path
			WinUtil.openFile(query);
			
//			Thread.sleep(3000);
//			App.getDefault().show();
		}
		catch (Exception ex) {
			Log.getInstance(this.getClass()).error(ex.getMessage());
		}
		return null;
	}

}
