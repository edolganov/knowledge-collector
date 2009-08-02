package ru.chapaj.tool.link.collector.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import ru.chapaj.tool.link.collector.App;
import ru.chapaj.tool.link.collector.AppUtil;
import ru.chapaj.tool.link.collector.command.OpenLinkByDefault;
import ru.chapaj.tool.link.collector.model.Link;
import ru.chapaj.util.log.Log;
import ru.chapaj.util.os.BareBonesBrowserLaunch;

public class LinkPopupMenuController extends Controller<JPopupMenu> implements ActionListener {

	@Override
	public void init(JPopupMenu ui) {
		
		JMenuItem mi = new JMenuItem("Open (default browser)");
        mi.addActionListener(this);
        mi.setActionCommand("default");
        ui.add(mi);
		
		mi = new JMenuItem("Firefox");
        mi.addActionListener(this);
        mi.setActionCommand("ff");
        ui.add(mi);
        
        mi = new JMenuItem("Opera");
        mi.addActionListener(this);
        mi.setActionCommand("opera");
        ui.add(mi);
        
        mi = new JMenuItem("IE");
        mi.addActionListener(this);
        mi.setActionCommand("explorer");
        ui.add(mi);
        
        ui.setOpaque(true);
        ui.setLightWeightPopupEnabled(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = App.getDefault().getTreeController().getCurrentTreeObject();
		if(! AppUtil.isLink(obj)) return;
		Link link = (Link) obj;
		String op = e.getActionCommand();
		if(op.equals("default")){
			new OpenLinkByDefault(link).invoke();
		}
		
	}

}
