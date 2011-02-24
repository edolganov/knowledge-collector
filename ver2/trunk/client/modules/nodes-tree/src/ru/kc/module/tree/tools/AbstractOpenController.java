package ru.kc.module.tree.tools;

import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import ru.kc.common.controller.Controller;
import ru.kc.util.swing.keyboard.EnterKey;
import ru.kc.util.swing.tree.TreeFacade;


public abstract class AbstractOpenController<N, T> extends Controller<T> {

	TreeFacade treeFacade;
	

	protected void init(final Class<N> type, JTree tree, final boolean preventExpandingChildren) {
		
		treeFacade = new TreeFacade(tree);
		
		tree.addMouseListener(new MouseAdapter(){
			
			
			@Override
			public void mouseClicked(MouseEvent e) {
				N node = treeFacade.getCurrentObject(type);
				if(node == null) return; 
				
				if(e.getClickCount() == 2 && treeFacade.isOnSelectedElement(e.getX(), e.getY())){
					open(node);
					if(preventExpandingChildren){
						preventExpandingChildren();
					}
					
				}
			}
			
		});
		
		tree.addKeyListener(new EnterKey() {
			
			@Override
			protected void doAction(KeyEvent e) {
				N node = treeFacade.getCurrentObject(type);
				if(node == null) return; 
				
				open(node);
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

	protected abstract void open(N node);
	

}
