package ru.kc.module.tree.tools;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

import ru.kc.common.node.NodeIcon;
import ru.kc.common.node.command.CreateDirRequest;
import ru.kc.common.node.command.DeleteNode;
import ru.kc.model.Dir;
import ru.kc.model.FileLink;
import ru.kc.model.Link;
import ru.kc.model.Node;
import ru.kc.model.Text;
import ru.kc.platform.command.CommandService;
import ru.kc.util.swing.icon.IconUtil;
import ru.kc.util.swing.tree.TreeFacade;

public class TreeMenu extends JPopupMenu {
	
	JMenuItem delete = new JMenuItem("delete",IconUtil.get("/ru/kc/common/img/delete.png"));
	
	JMenuItem info = new JMenuItem();
	
	JMenuItem rename = new JMenuItem("rename");
	
	JMenu addMenu = new JMenu("add");
	JMenuItem dir = new JMenuItem("dir",NodeIcon.getIcon(Dir.class));
	JMenuItem text = new JMenuItem("text",NodeIcon.getIcon(Text.class));
	JMenuItem link = new JMenuItem("link",NodeIcon.getIcon(Link.class));
	JMenuItem fileLink = new JMenuItem("file link",NodeIcon.getIcon(FileLink.class));
	
	JMenu parent = new JMenu("parent ops");
	JMenuItem dirToParent = new JMenuItem("dir",NodeIcon.getIcon(Dir.class));
	JMenuItem textToParent = new JMenuItem("text",NodeIcon.getIcon(Text.class));
	JMenuItem linkToParent = new JMenuItem("link",NodeIcon.getIcon(Link.class));
	JMenuItem fileLinkToParent = new JMenuItem("file link",NodeIcon.getIcon(FileLink.class));
	
	JMenuItem showHideInfo = new JMenuItem("");
	ImageIcon leftIcon = IconUtil.get("/ru/kc/common/img/left.png");
	ImageIcon rightIcon = IconUtil.get("/ru/kc/common/img/right.png");
	
	boolean showInfo = true;
	
	TreeFacade treeFacade;
	
	public TreeMenu(JTree tree, final CommandService commandService) {
		treeFacade = new TreeFacade(tree);
		
		rename.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JTree tree = treeFacade.tree;
				TreeCellEditor treeCellEditor = tree.getCellEditor();
				if(treeCellEditor instanceof CellEditor){
					CellEditor cellEditor = (CellEditor)treeCellEditor;
					cellEditor.setEnabledRequest();
					TreePath selectionPath = tree.getSelectionPath();
					tree.startEditingAtPath(selectionPath);
				}
			}
		});
		
		delete.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultMutableTreeNode treeNode = treeFacade.getCurrentNode();
				if(treeNode.isRoot()) return;
				
				Node node = treeFacade.getCurrentObject(Node.class);
				commandService.invokeSafe(new DeleteNode(node));
			}
			
		});
		
		dir.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Node parent = treeFacade.getCurrentObject(Node.class);
				commandService.invokeSafe(new CreateDirRequest(parent));
			}
			
		});
		
		text.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				//CommandService.invoke(new AddTreeNode(DialogOps.newText()));
			}
			
		});
		
		link.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				//CommandService.invoke(new AddTreeNode(DialogOps.newLink()));
			}
			
		});
		
		addMenu.add(dir);
		addMenu.add(text);
		addMenu.add(link);
		addMenu.add(fileLink);
		
		dirToParent.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Node parent = getParentOfCurrentNode();
				commandService.invokeSafe(new CreateDirRequest(parent));
			}
			
		});
		
		textToParent.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				//Node parent = tree.getParentObject(tree.getCurrentNode(), Node.class);
				//CommandService.invoke(new AddTreeNode(parent,DialogOps.newText()));
			}
			
		});
		
		linkToParent.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				//Node parent = tree.getParentObject(tree.getCurrentNode(), Node.class);
				//CommandService.invoke(new AddTreeNode(parent,DialogOps.newLink()));
			}
			
		});
		
		parent.add(dirToParent);
		parent.add(textToParent);
		parent.add(linkToParent);
		parent.add(fileLinkToParent);
		
		showHideInfo.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				//App.getDefault().fireAction(this, "need-show-hide-info-action");
			}
			
		});
//		App.getDefault().addListener(new AppListener(){
//
//			@Override
//			public void onAction(Object source, String action, Object... data) {
//				if("hide-info".equals(action)){
//					showInfo = false;
//				}
//				else if("show-info".equals(action)){
//					showInfo = true;
//				}
//			}
//			
//		});
		
		
	}
	
	protected Node getParentOfCurrentNode() {
		DefaultMutableTreeNode currentNode = treeFacade.getCurrentNode();
		if(currentNode != null){
			DefaultMutableTreeNode parent = (DefaultMutableTreeNode)currentNode.getParent();
			if(parent != null){
				Object ob = parent.getUserObject();
				if(ob instanceof Node){
					return (Node)ob;
				}
			}
		}
		return null;
	}


	@Override
	public void show(Component invoker, int x, int y) {
		DefaultMutableTreeNode currentNode = treeFacade.getCurrentNode();
		if(currentNode == null) return;
		
		removeAll();
		
		add(rename);
		
		add(addMenu);
		
		boolean isRootNode = currentNode.isRoot();
		parent.setEnabled(!isRootNode);
		add(parent);
		
		if(showInfo){
			showHideInfo.setText("hide info panel");
			showHideInfo.setIcon(rightIcon);
		} else {
			showHideInfo.setText("show info panel");
			showHideInfo.setIcon(leftIcon);
		}
		add(showHideInfo);
		addSeparator();
		
		delete.setEnabled(!isRootNode);
		add(delete);
		
		
		
		super.show(invoker, x, y);
	}
	
	

}
