package ru.kc.main.tab.tools;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;

import ru.kc.common.Context;
import ru.kc.platform.app.AppContext;
import ru.kc.platform.domain.DomainMember;
import ru.kc.util.swing.icon.IconUtil;

@SuppressWarnings("serial")
public class MainMenu extends JPopupMenu {

	JMenuItem delete = new JMenuItem("Delete  (Delete)", IconUtil.get("/ru/kc/common/img/delete.png"));

	public MainMenu() {


		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
//				DefaultMutableTreeNode treeNode = treeFacade.getCurrentNode();
//				if (treeNode.isRoot())
//					return;
//
//				Node node = treeFacade.getCurrentObject(Node.class);
//				commandService.invokeSafe(new DeleteNode(node));
			}

		});


	}

	@Override
	public void show(Component invoker, int x, int y) {
		add(delete);

		super.show(invoker, x, y);
	}

}
