package ru.kc.module.tree.tools;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

import ru.kc.common.Context;
import ru.kc.common.dashboard.event.OpenHideRightPanel;
import ru.kc.common.node.NodeIcon;
import ru.kc.common.node.command.CreateDirRequest;
import ru.kc.common.node.command.CreateFileLinkRequest;
import ru.kc.common.node.command.CreateLinkRequest;
import ru.kc.common.node.command.CreateTextRequest;
import ru.kc.common.node.command.DeleteNode;
import ru.kc.common.node.edit.NodeEditionsAggregator;
import ru.kc.common.node.edit.event.UpdateNodeRequest;
import ru.kc.common.node.edit.event.RevertNodeRequest;
import ru.kc.model.Dir;
import ru.kc.model.FileLink;
import ru.kc.model.Link;
import ru.kc.model.Node;
import ru.kc.model.Text;
import ru.kc.module.tree.command.RenameCellRequestCommand;
import ru.kc.platform.app.AppContext;
import ru.kc.platform.command.CommandService;
import ru.kc.platform.domain.DomainMember;
import ru.kc.platform.event.EventManager;
import ru.kc.util.swing.icon.IconUtil;
import ru.kc.util.swing.tree.TreeFacade;

@SuppressWarnings("serial")
public class TreeMenu extends JPopupMenu {

	JMenuItem delete = new JMenuItem("Delete  (Delete)", IconUtil.get("/ru/kc/common/img/delete.png"));

	JMenuItem info = new JMenuItem();

	JMenuItem save = new JMenuItem("Save  (Ctrl+S)", IconUtil.get("/ru/kc/common/img/save.png"));
	JMenuItem revert = new JMenuItem("Revert", IconUtil.get("/ru/kc/common/img/revert.png"));
	
	JMenuItem rename = new JMenuItem("Rename  (Ctrl+R)", IconUtil.get("/ru/kc/common/img/rename.png"));

	JMenu addMenu = new JMenu("Add");
	JMenuItem dir = new JMenuItem("Dir", NodeIcon.getIcon(Dir.class));
	JMenuItem text = new JMenuItem("Text", NodeIcon.getIcon(Text.class));
	JMenuItem link = new JMenuItem("Link", NodeIcon.getIcon(Link.class));
	JMenuItem fileLink = new JMenuItem("File link",
			NodeIcon.getIcon(FileLink.class));

	JMenu parent = new JMenu("Parent ops");
	JMenuItem dirToParent = new JMenuItem("Dir", NodeIcon.getIcon(Dir.class));
	JMenuItem textToParent = new JMenuItem("Text", NodeIcon.getIcon(Text.class));
	JMenuItem linkToParent = new JMenuItem("Link", NodeIcon.getIcon(Link.class));
	JMenuItem fileLinkToParent = new JMenuItem("File link",
			NodeIcon.getIcon(FileLink.class));

	JMenuItem showHideInfo = new JMenuItem("Show/hide right panel  (Ctrl+H)", IconUtil.get("/ru/kc/common/img/right_panel.png"));


	TreeFacade treeFacade;
	NodeEditionsAggregator nodeEditionsAggregator;

	public TreeMenu(JTree tree, AppContext appContext, Context context, final DomainMember domainMember) {
		treeFacade = new TreeFacade(tree);

		final CommandService commandService = appContext.commandService;
		final EventManager events = appContext.eventManager;
		nodeEditionsAggregator = context.nodeEditionsAggregator;
		
		rename.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				commandService.invokeSafe(new RenameCellRequestCommand(treeFacade.tree));
			}
		});
		
		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Node node = treeFacade.getCurrentObject(Node.class);
				if(node != null){
					events.fireEventInEDT(domainMember, new UpdateNodeRequest(node));
				}
			}
		});
		
		revert.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Node node = treeFacade.getCurrentObject(Node.class);
				if(node != null){
					events.fireEventInEDT(domainMember, new RevertNodeRequest(node));
				}
			}
		});

		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode treeNode = treeFacade.getCurrentNode();
				if (treeNode.isRoot())
					return;

				Node node = treeFacade.getCurrentObject(Node.class);
				commandService.invokeSafe(new DeleteNode(node));
			}

		});

		dir.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Node parent = treeFacade.getCurrentObject(Node.class);
				commandService.invokeSafe(new CreateDirRequest(parent));
			}

		});

		text.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Node parent = treeFacade.getCurrentObject(Node.class);
				commandService.invokeSafe(new CreateTextRequest(parent));
			}

		});

		link.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Node parent = treeFacade.getCurrentObject(Node.class);
				commandService.invokeSafe(new CreateLinkRequest(parent));
			}

		});
		
		fileLink.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Node parent = treeFacade.getCurrentObject(Node.class);
				commandService.invokeSafe(new CreateFileLinkRequest(parent));
			}

		});

		addMenu.add(dir);
		addMenu.add(text);
		addMenu.add(link);
		addMenu.add(fileLink);

		dirToParent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Node parent = getParentOfCurrentNode();
				commandService.invokeSafe(new CreateDirRequest(parent));
			}

		});

		textToParent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Node parent = getParentOfCurrentNode();
				commandService.invokeSafe(new CreateTextRequest(parent));
			}

		});

		linkToParent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Node parent = getParentOfCurrentNode();
				commandService.invokeSafe(new CreateLinkRequest(parent));
			}

		});
		
		fileLinkToParent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Node parent = getParentOfCurrentNode();
				commandService.invokeSafe(new CreateFileLinkRequest(parent));
			}

		});

		parent.add(dirToParent);
		parent.add(textToParent);
		parent.add(linkToParent);
		parent.add(fileLinkToParent);

		showHideInfo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				events.fireEventInEDT(domainMember, new OpenHideRightPanel());
			}

		});
		// App.getDefault().addListener(new AppListener(){
		//
		// @Override
		// public void onAction(Object source, String action, Object... data) {
		// if("hide-info".equals(action)){
		// showInfo = false;
		// }
		// else if("show-info".equals(action)){
		// showInfo = true;
		// }
		// }
		//
		// });

	}

	protected Node getParentOfCurrentNode() {
		DefaultMutableTreeNode currentNode = treeFacade.getCurrentNode();
		if (currentNode != null) {
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode) currentNode
					.getParent();
			if (parent != null) {
				Object ob = parent.getUserObject();
				if (ob instanceof Node) {
					return (Node) ob;
				}
			}
		}
		return null;
	}

	@Override
	public void show(Component invoker, int x, int y) {
		DefaultMutableTreeNode currentNode = treeFacade.getCurrentNode();
		if (currentNode == null)
			return;
		Object userObject = currentNode.getUserObject();
		if( ! (userObject instanceof Node))
			return;
		
		Node node = (Node) userObject;

		removeAll();
		
		if(nodeEditionsAggregator.hasEditions(node)){
			add(revert);
			add(save);
			addSeparator();
		}

		add(rename);

		add(addMenu);

		boolean isRootNode = currentNode.isRoot();
		parent.setEnabled(!isRootNode);
		add(parent);
		add(showHideInfo);
		addSeparator();

		delete.setEnabled(!isRootNode);
		add(delete);

		super.show(invoker, x, y);
	}

}
