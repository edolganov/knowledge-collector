package ru.kc.module.tree.tools;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import ru.kc.common.controller.Controller;
import ru.kc.common.tree.TreeNodeFacade;
import ru.kc.common.tree.TreeService;
import ru.kc.common.tree.event.GetTreeServiceRequest;
import ru.kc.module.tree.ui.Tree;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.util.swing.tree.TreeFacade;
import ru.kc.util.swing.tree.TreeUtil;

@Mapping(Tree.class)
public class TreeServiceController extends Controller<Tree> implements TreeService {

	private static class TreeNodeFacadeImpl implements TreeNodeFacade {
		
		private DefaultMutableTreeNode node;
		private TreeFacade treeFacade;

		public TreeNodeFacadeImpl(DefaultMutableTreeNode node,
				TreeFacade treeFacade) {
			super();
			this.node = node;
			this.treeFacade = treeFacade;
		}

		@Override
		public <T> T getUserObject(Class<T> type) {
			return TreeUtil.getUserObject(node, type);
		}

		@Override
		public boolean isOpen() {
			return treeFacade.isExpanded(node);
		}
		
		
	}
	
	TreeFacade treeFacade;
	
	@Override
	protected void init() {
		treeFacade = new TreeFacade(ui.tree);
	}
	
	@EventListener
	public void getTreeService(GetTreeServiceRequest request){
		request.setResponse(this);
	}

	@Override
	public TreeNodeFacade getRoot() {
		return new TreeNodeFacadeImpl(treeFacade.getRoot(), treeFacade);
	}

	@Override
	public List<TreeNodeFacade> getChildren(TreeNodeFacade node) {
		if(node instanceof TreeNodeFacadeImpl){
			
			ArrayList<TreeNodeFacade> out = new ArrayList<TreeNodeFacade>();
			
			TreeNodeFacadeImpl nodeImpl = (TreeNodeFacadeImpl) node;
			for(int i = 0; i < nodeImpl.node.getChildCount(); i++){
				DefaultMutableTreeNode child = (DefaultMutableTreeNode)nodeImpl.node.getChildAt(i);
				out.add(new TreeNodeFacadeImpl(child, treeFacade));
			}
			
			return out;
		} else {
			throw new IllegalArgumentException("unknow type of object "+node);
		}
	}

}
