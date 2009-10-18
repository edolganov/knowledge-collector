package ru.dolganov.tool.knowledge.collector.tree;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
public class ClickController extends Controller<MainWindow> implements ClipboardOwner{

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
		
		ui.tree.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_C && e.getModifiersEx() == KeyEvent.CTRL_DOWN_MASK){
					//System.out.println("ctrl+c");
					NodeMeta meta = ui.tree.getCurrentObject(NodeMeta.class);
					if(meta != null && meta.getName() != null){
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					    clipboard.setContents( new StringSelection(meta.getName()), ClickController.this );
					}
				}
			}
		});
		
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		
	}

}
