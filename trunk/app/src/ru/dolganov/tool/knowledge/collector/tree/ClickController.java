package ru.dolganov.tool.knowledge.collector.tree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import model.knowledge.Link;
import model.knowledge.NodeMeta;

import ru.chapaj.util.os.BareBonesBrowserLaunch;
import ru.chapaj.util.os.win.WinUtil;
import ru.dolganov.tool.knowledge.collector.Controller;
import ru.dolganov.tool.knowledge.collector.annotation.ControllerInfo;
import ru.dolganov.tool.knowledge.collector.main.MainWindow;

@ControllerInfo(target=MainWindow.class,dependence=TreeController.class)
public class ClickController extends Controller<MainWindow>{

	@Override
	public void init(final MainWindow ui) {
		ui.tree.addMouseListener(new MouseAdapter(){
			
			//long firstClick = -1;
			//long secondClick = -1;
			
			@Override
			public void mouseClicked(MouseEvent e) {
				//System.out.println(e.getClickCount());
				if (e.getClickCount() == 2) {
					//handle double click. 
					NodeMeta node = ui.tree.getCurrentObject(NodeMeta.class);
					if(node instanceof Link){
						try{
							String query = ((Link)node).getUrl();
							//it's url
							if(query.startsWith("http") || query.startsWith("www")){
								BareBonesBrowserLaunch.openURL(query);
							}
							//it's local path
							else WinUtil.openFile(query);
						}catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			}
			
			
		});
		
	}

}
