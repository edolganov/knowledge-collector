package ru.kc.module.snapshots.tools;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

import ru.kc.module.snapshots.command.DeleteTreeObject;
import ru.kc.module.snapshots.command.UpdateSnapshot;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.platform.app.AppContext;
import ru.kc.platform.command.CommandService;
import ru.kc.platform.domain.DomainMember;
import ru.kc.util.swing.icon.IconUtil;
import ru.kc.util.swing.tree.TreeFacade;

@SuppressWarnings("serial")
public class TreeMenu extends JPopupMenu {

	JMenuItem rename = new JMenuItem("Rename", IconUtil.get("/ru/kc/common/img/rename.png"));
	JMenuItem resnapshot = new JMenuItem("Refresh snapshot", IconUtil.get("/ru/kc/common/img/refresh.png"));
	JMenuItem delete = new JMenuItem("Delete  (Delete)", IconUtil.get("/ru/kc/common/img/delete.png"));
	
	TreeFacade treeFacade;

	public TreeMenu(JTree tree, AppContext appContext, final DomainMember domainMember) {
		treeFacade = new TreeFacade(tree);

		final CommandService commandService = appContext.commandService;
		
		rename.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						JTree tree = treeFacade.tree;
						TreeCellEditor treeCellEditor = tree.getCellEditor();
						if (treeCellEditor instanceof CellEditor) {
							CellEditor cellEditor = (CellEditor) treeCellEditor;
							cellEditor.setEnabledRequest();
							TreePath selectionPath = tree.getSelectionPath();
							tree.startEditingAtPath(selectionPath);
						}
					}
				});
			}
		});

		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DeleteTreeObject command = new DeleteTreeObject(treeFacade.tree);
				command.setDomainMember(domainMember);
				commandService.invokeSafe(command);
			}

		});
		
		resnapshot.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode node = treeFacade.getCurrentNode();
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode)node.getParent();
				Snapshot snapshot = (Snapshot) node.getUserObject();
				SnapshotDir dir = (SnapshotDir)parent.getUserObject();
				UpdateSnapshot command = new UpdateSnapshot(dir, snapshot);
				command.setDomainMember(domainMember);
				commandService.invokeSafe(command);
			}

		});

	}

	@Override
	public void show(Component invoker, int x, int y) {
		DefaultMutableTreeNode currentNode = treeFacade.getCurrentNode();
		if (currentNode == null)
			return;

		removeAll();
		
		Object ob = currentNode.getUserObject();

		if(ob instanceof Snapshot){
			add(rename);
			add(resnapshot);
			add(delete);
		} 
		else if(ob instanceof SnapshotDir){
			add(rename);
			add(delete);
		}


		super.show(invoker, x, y);
	}

}
