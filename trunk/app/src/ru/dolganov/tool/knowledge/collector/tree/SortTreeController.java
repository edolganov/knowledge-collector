package ru.dolganov.tool.knowledge.collector.tree;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import model.knowledge.Dir;
import model.knowledge.Link;
import model.knowledge.NodeMeta;
import model.knowledge.Root;
import model.knowledge.TextData;
import ru.chapaj.util.swing.tree.ExtendDefaultTreeModel;
import ru.dolganov.tool.knowledge.collector.Controller;
import ru.dolganov.tool.knowledge.collector.annotation.ControllerInfo;
import ru.dolganov.tool.knowledge.collector.dao.DAOEventAdapter;
import ru.dolganov.tool.knowledge.collector.main.MainWindow;

@ControllerInfo(target=MainWindow.class, dependence=TreeController.class)
public class SortTreeController extends Controller<MainWindow>{
	
	
	private Comparator<NodeMeta> nodeComparator = new Comparator<NodeMeta>(){

		@Override 
		public int compare(NodeMeta a,NodeMeta b){
			int na = index(a);
			int nb = index(b);
			return (na<nb ? -1 : (na==nb ? 0 : 1));
		}
		
		private int index(NodeMeta meta){
			int out = Integer.MAX_VALUE;
			if(meta instanceof Dir) out = 0;
			else if(meta instanceof TextData) out = 10;
			else if(meta instanceof Link) out = 20;
			return out;
		}
		
	};
	
	MainWindow ui;
	
	@Override
	public void init(MainWindow ui_) {
		ui = ui_;
		get(TreeController.class).addListener(new DAOEventAdapter(){

			@Override
			public void onAdded(NodeMeta parent, NodeMeta child) {
				//update model
				Root root = child.getParent();
				List<NodeMeta> nodes = root.getNodes();
				Collections.sort(nodes, nodeComparator);
				dao.merge(root, null);
				//update tree
				DefaultMutableTreeNode parentNode = dao.getCache().get(parent, "tree-node", DefaultMutableTreeNode.class);
				for (int i = 0; i < nodes.size(); i++) {
					DefaultMutableTreeNode childNode = dao.getCache().get(nodes.get(i), "tree-node", DefaultMutableTreeNode.class);
					if(parentNode.getChildAt(i) != childNode){
						parentNode.remove(childNode);
						parentNode.insert(childNode, i);
					}					
				}
				ui.tree.model().reload(parentNode);
			}
		});
		
	}

}
