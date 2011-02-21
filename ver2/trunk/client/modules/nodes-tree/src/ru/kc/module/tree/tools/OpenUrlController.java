package ru.kc.module.tree.tools;

import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import ru.kc.common.controller.Controller;
import ru.kc.model.Link;
import ru.kc.module.tree.ui.Tree;
import ru.kc.platform.annotations.Mapping;
import ru.kc.util.swing.keyboard.EnterKey;
import ru.kc.util.swing.os.OSUtil;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(Tree.class)
public class OpenUrlController extends Controller<Tree> {

	TreeFacade treeFacade;
	
	@Override
	protected void init() {
		
		treeFacade = new TreeFacade(ui.tree);
		
		ui.tree.addMouseListener(new MouseAdapter(){
			
			
			@Override
			public void mouseClicked(MouseEvent e) {
				Link link = treeFacade.getCurrentObject(Link.class);
				if(link == null) return; 
				
				if(e.getClickCount() == 2 && treeFacade.isOnSelectedElement(e.getX(), e.getY())){
					openUrl(link);
					preventExpandingChildren();
				}
			}
			
		});
		
		ui.tree.addKeyListener(new EnterKey() {
			
			@Override
			protected void doAction(KeyEvent e) {
				Link link = treeFacade.getCurrentObject(Link.class);
				if(link == null) return; 
				
				openUrl(link);
			}
		});
		

	}

	protected void preventExpandingChildren() {
		DefaultMutableTreeNode node = treeFacade.getCurrentNode();
		TreePath path = new TreePath(node.getPath());
		JTree tree = treeFacade.tree;
		boolean expanded = tree.isExpanded(path);
		if(expanded){
			tree.collapsePath(path);
		} else {
			tree.expandPath(path);
		}
		
	}

	protected void openUrl(Link link) {
		try{
			OSUtil.openUrl(link.getUrl());
		}catch (Exception e) {
			log.error("can't open url", e);
		}
		
	}
	

}
