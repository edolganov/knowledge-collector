package ru.kc.module.snapshots.tools;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;


import javax.swing.tree.DefaultMutableTreeNode;

import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.util.swing.tree.AbstarctTreeTransferHandler;
import ru.kc.util.swing.tree.TransferInfo;
import ru.kc.util.swing.tree.TransferObject;

@SuppressWarnings("serial")
public class TreeTransferHandler extends AbstarctTreeTransferHandler {
	
	
	private static final DataFlavor NODE_FLAVOR = new DataFlavor(Snapshot.class,"snaphot");
	
	public TreeTransferHandler(Listener listener) {
		super(listener);
	}
    
	protected Transferable createTransferable(DefaultMutableTreeNode treeNode) { 
    	Object userObject = treeNode.getUserObject();
    	if(userObject instanceof Snapshot){
    		Snapshot snapshot = (Snapshot)userObject;
    		TransferInfo info1 = new TransferInfo(NODE_FLAVOR, snapshot);
    		TransferInfo info2 = new TransferInfo(DataFlavor.stringFlavor, snapshot.getName());
			return new TransferObject (info1, info2);
    	} else {
            return new StringSelection(userObject.toString()); 
    	}
    } 


	protected boolean canImport(DataFlavor[] transferDataFlavors) { 
		for(DataFlavor dataFlavor : transferDataFlavors){
			if(dataFlavor.equals(DataFlavor.stringFlavor)){
				return true;
			}
			
			if(dataFlavor.equals(NODE_FLAVOR)){
				return true;
			}
		}
		return false;
    }

	@Override
	protected Object getImportData(TransferSupport info) throws Exception {
		Transferable transferable = info.getTransferable();
		if(transferable.isDataFlavorSupported(NODE_FLAVOR)){
			return transferable.getTransferData(NODE_FLAVOR);
		}
		
		if(transferable.isDataFlavorSupported(DataFlavor.stringFlavor)){
			return transferable.getTransferData(DataFlavor.stringFlavor);
		}
		
		return new Object(); //unknow
	}

}
