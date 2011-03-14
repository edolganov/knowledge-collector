package ru.kc.module.tree.tools;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import ru.kc.common.controller.Controller;
import ru.kc.common.search.event.SearchRequest;
import ru.kc.model.FileLink;
import ru.kc.model.Link;
import ru.kc.model.Node;
import ru.kc.module.tree.ui.Tree;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(Tree.class)
public class SearchController extends Controller<Tree> {
	
	
	TreeFacade treeFacade;
	
	@Override
	protected void init() {
		treeFacade = new TreeFacade(ui.tree);

	}
	
	
	@EventListener
	public void onSearchRequest(SearchRequest request){
		List<String> likeElements = request.likeElements;
		likeElements = prepare(likeElements);
		
		DefaultMutableTreeNode root = treeFacade.getRoot();
		List<Node> out = process(root, likeElements);
		
		request.setResponse(out);
	}


	private List<String> prepare(List<String> likeElements) {
		ArrayList<String> out = new ArrayList<String>();
		for(String element : likeElements){
			String lowerCase = element.toLowerCase();
			out.add(lowerCase);
		}
		return out;
	}


	private List<Node> process(DefaultMutableTreeNode node, List<String> likeElements) {
		ArrayList<Node> out = new ArrayList<Node>();
		
		LinkedList<DefaultMutableTreeNode> queue = new LinkedList<DefaultMutableTreeNode>();
		queue.addLast(node);
		while(!queue.isEmpty()){
			DefaultMutableTreeNode curNode = queue.removeFirst();
			Object ob = curNode.getUserObject();
			if(ob instanceof Node){
				Node candidat = (Node)ob;
				if(contains(candidat, likeElements)){
					out.add(candidat);
				}
			}
			for(int i=0; i < curNode.getChildCount(); i++){
				queue.addLast((DefaultMutableTreeNode)curNode.getChildAt(i));
			}
		}
		
		return out;
	}


	private boolean contains(Node node, List<String> likeElements) {
		if(contains(node.getName(), likeElements))
			return true;
		if(contains(node.getDescription(), likeElements))
			return true;
		if(node instanceof Link){
			String url = ((Link) node).getUrl();
			if(contains(url, likeElements))
				return true;
		}
		if(node instanceof FileLink){
			String path = ((FileLink) node).getPath();
			if(contains(path, likeElements))
				return true;
		}
		
		return false;
	}


	private boolean contains(String value, List<String> likeElements) {
		if(value == null || value.length() == 0) 
			return false;
		
		value = value.toLowerCase();
		
		for(String element : likeElements){
			if(value.contains(element))
				return true;
		}
		
		return false;
	}


}
