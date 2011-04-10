package ru.kc.module.imports.oldclient.chain;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ru.kc.model.Node;
import ru.kc.module.imports.oldclient.oldmodel.OldDir;
import ru.kc.module.imports.oldclient.oldmodel.OldLocalLink;
import ru.kc.module.imports.oldclient.oldmodel.OldNetworkLink;
import ru.kc.module.imports.oldclient.oldmodel.OldNote;
import ru.kc.module.imports.oldclient.oldmodel.Root;
import ru.kc.module.imports.oldclient.oldmodel.RootElement;
import ru.kc.util.workflow.ChainObject;

public class ProcessData extends AbstractChainObject {
	
	File parentDir;
	
	public ProcessData(File parentDir) {
		super();
		this.parentDir = parentDir;
	}

	@Override
	public ChainObject getInvokeAndGetNext() throws Exception {
		File dataDir = getDataFile(parentDir);
		if(dataDir == null)
			return null;
		
		Root root = dataLoader.loadFile(dataDir);
		List<RootElement> added = addNodesToTree(root);
		
		
		
		return null;
	}

	private List<RootElement> addNodesToTree(Root root) {
		ArrayList<RootElement> added = new ArrayList<RootElement>();
		
		Node rootNode = getCurRootNode();
		for(RootElement elem : root.getNodes()){
			if(elem instanceof OldDir){
				add(rootNode, (OldDir)elem);
			}
			else if(elem instanceof OldNote){
				add(rootNode, (OldNote)elem);
			}
			else if(elem instanceof OldNetworkLink){
				add(rootNode, (OldNetworkLink)elem);
			}
			else if(elem instanceof OldLocalLink){
				add(rootNode, (OldLocalLink)elem);
				
			} else {
				log.info("can't add unknow node "+elem);
				continue;
			}
			added.add(elem);
		}
		
		return added;
	}

	private void add(Node rootNode, OldDir elem) {
		
		
	}
	
	private void add(Node rootNode, OldNote elem) {
		// TODO Auto-generated method stub
		
	}
	
	private void add(Node rootNode, OldNetworkLink elem) {
		// TODO Auto-generated method stub
		
	}
	
	private void add(Node rootNode, OldLocalLink elem) {
		// TODO Auto-generated method stub
		
	}

	private Node getCurRootNode() {
		Node out = (Node)invokeContext.get("root-node");
		if(out == null){
			out = beginRootNode;
			invokeContext.put("root-node", out);
		}
		return out;
	}
	
	

}
