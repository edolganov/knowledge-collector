package ru.dolganov.tool.knowledge.collector.tree;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import ru.chapaj.util.swing.IconHelper;
import ru.chapaj.util.swing.tree.ExtendTree;
import ru.dolganov.tool.knowledge.collector.actions.DeleteCurrentTreeElementAction;
import ru.dolganov.tool.knowledge.collector.tree.cell.HasCellConst;

public class TreeMenu extends JPopupMenu implements HasCellConst {
	
	ExtendTree tree;
	JMenuItem delete = new JMenuItem("delete",IconHelper.get("/images/kc/tree/menu/delete.png"));

	public TreeMenu(ExtendTree tree) {
		super();
		this.tree = tree;
		
		delete.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				new DeleteCurrentTreeElementAction();
			}
			
		});
	}


	@Override
	public void show(Component invoker, int x, int y) {
		Object ob = tree.getCurrentObject();
		if(Cell.BUTTONS == ob) return;
		
		removeAll();
		add(delete);
		
		super.show(invoker, x, y);
	}
	
	

}
