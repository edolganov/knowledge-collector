package ru.dolganov.tool.knowledge.collector.tree;

import java.awt.Component;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import model.knowledge.Dir;
import model.knowledge.LocalLink;
import model.knowledge.NetworkLink;
import model.knowledge.NodeMeta;
import model.knowledge.Note;
import ru.chapaj.util.swing.IconHelper;
import ru.chapaj.util.swing.tree.ExtendTree;
import ru.chapaj.util.swing.tree.ExtendTree.SelectModel;
import ru.dolganov.tool.knowledge.collector.Controller;
import ru.dolganov.tool.knowledge.collector.main.MainWindow;

public class TreeController extends Controller<MainWindow>{

	ExtendTree tree;
	DefaultMutableTreeNode treeRoot;
	
	
	@Override
	public void init(MainWindow ui) {
		tree = ui.tree;
		tree.init(ExtendTree.createTreeModel(null), true, new TreeCellRender(), SelectModel.SINGLE);
		treeRoot = tree.getRootNode();
		treeRoot.setUserObject("root");
		tree.setRootVisible(false);
		fillTree();
	}


	static class QS {
		List<NodeMeta> list;
		DefaultMutableTreeNode node;
		public QS(List<NodeMeta> list, DefaultMutableTreeNode node) {
			super();
			this.list = list;
			this.node = node;
		}
	}
	
	private void fillTree() {
		treeRoot.removeAllChildren();
		long time = System.currentTimeMillis();
		
		LinkedList<QS> q = new LinkedList<QS>();
		q.addLast(new QS(dao.getRoot().getNodes(),treeRoot));
		while(!q.isEmpty()){
			QS s = q.removeFirst();
			for(NodeMeta meta : s.list){
				DefaultMutableTreeNode chNode = new DefaultMutableTreeNode(meta);
				s.node.add(chNode);
				q.addLast(new QS(dao.getChildren(meta),chNode));
			}
		}
		System.out.println("tree filled after "
				+ ((System.currentTimeMillis() - time) / 1000.) + " sec");
		
		tree.expandPath(treeRoot);
		tree.updateUI();
		
	}

	
}

class TreeCellRender extends DefaultTreeCellRenderer {
	
	ImageIcon dir = IconHelper.get("/images/kc/tree/dir.png");
    ImageIcon netLink = IconHelper.get("/images/kc/tree/netLink.png");
    ImageIcon note = IconHelper.get("/images/kc/tree/note.png");
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
		add(new JButton("test"));
		return this;
		
	}
}
