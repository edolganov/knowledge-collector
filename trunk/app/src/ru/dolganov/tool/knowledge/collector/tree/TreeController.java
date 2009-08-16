package ru.dolganov.tool.knowledge.collector.tree;

import java.awt.Component;
import java.util.LinkedList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.plaf.metal.MetalIconFactory;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import model.knowledge.Dir;
import model.knowledge.LocalLink;
import model.knowledge.NetworkLink;
import model.knowledge.NodeMeta;
import model.knowledge.Note;
import ru.chapaj.util.swing.tree.ExtendTree;
import ru.chapaj.util.swing.tree.ExtendTree.SelectModel;
import ru.dolganov.tool.knowledge.collector.Controller;
import ru.dolganov.tool.knowledge.collector.main.MainWindow;

public class TreeController extends Controller<MainWindow>{

	ExtendTree tree;
	DefaultMutableTreeNode treeRoot;
	
	
	@Override
	public void init(MainWindow ui) {
		tree = ui.jTree;
		tree.init(ExtendTree.createTreeModel(null), true, new TreeCellRender(), SelectModel.SINGLE);
		treeRoot = tree.getRootNode();
			
		fillTree();
	}


	static class QS {
		Dir dir;
		DefaultMutableTreeNode node;
		public QS(Dir dir, DefaultMutableTreeNode node) {
			super();
			this.dir = dir;
			this.node = node;
		}
	}
	private void fillTree() {
		treeRoot.removeAllChildren();
		Dir root = dao.getRoot().getRoot();
		treeRoot.setUserObject(root);
		
		LinkedList<QS> q = new LinkedList<QS>();
		q.addLast(new QS(root,treeRoot));
		while(!q.isEmpty()){
			QS s = q.removeFirst();
			for(NodeMeta meta : s.dir.getNodes()){
				DefaultMutableTreeNode chNode = new DefaultMutableTreeNode(meta);
				s.node.add(chNode);
				if (meta instanceof Dir) {
					q.addLast(new QS((Dir) meta,chNode));
				}
			}
			
		}
		
		tree.expandPath(treeRoot);
		tree.updateUI();
		
	}

	
}

class TreeCellRender extends DefaultTreeCellRenderer {
	
	ImageIcon dir = new ImageIcon(this.getClass().getResource("/images/kc/tree/dir.png"));
    ImageIcon netLink = new ImageIcon(this.getClass().getResource("/images/kc/tree/netLink.png"));
    ImageIcon note = new ImageIcon(this.getClass().getResource("/images/kc/tree/note.png"));
    //Icon otherIcon = MetalIconFactory.getTreeLeafIcon();
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
				row, hasFocus);
		Object obj = ((DefaultMutableTreeNode)value).getUserObject();
		if(obj instanceof Dir){
			setIcon(dir);
		}
		else if(obj instanceof Note){
			setIcon(note);
		}
		else if(obj instanceof NetworkLink){
			setIcon(netLink);
		}
		else if(obj instanceof LocalLink){
			setIcon(netLink);
		}
		else {
			setIcon(getLeafIcon());
		}
		return this;
		
	}
}
