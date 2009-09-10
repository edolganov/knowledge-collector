package ru.dolganov.tool.knowledge.collector.tree.cell;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.UIManager;

import ru.chapaj.util.swing.IconHelper;

public class NodeButtonsPanelExtend extends NodeButtonsPanel {
	
	public NodeButtonsPanelExtend() {
		super();
		Color color = UIManager.getColor("Tree.textBackground");
		setBackground(color);

		customize(dirB, color,"/images/kc/tree/dir_small_gray.png");
		customize(linkB, color,"/images/kc/tree/netLink_small_gray.png");
		customize(noteB, color,"/images/kc/tree/note_small_gray.png");
		
	}
	
	void customize(JButton b,Color color, String imagePath){
		int w=16,h=16;
		b.setIcon(IconHelper.get(imagePath));
		b.setSize(w, h);
		b.setFocusable(false);
		b.setBackground(color);
		b.setBorderPainted(false);
	}
	
	

}
