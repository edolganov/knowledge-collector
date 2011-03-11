package ru.kc.module.tree.tools;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import ru.kc.common.dnd.NodeSelection;
import ru.kc.model.Node;

@SuppressWarnings("serial")
public class TreeTransferHandler extends TransferHandler {
	
	public static interface Listener {
		
		void onCopy(DefaultMutableTreeNode destination, Object ob);
		
		void onMove(DefaultMutableTreeNode destination, Object ob);
	}
	
	
	private static final DataFlavor NODE_FLAVOR = new DataFlavor(Node.class,"node");
	
	private boolean cutAction;
	private Listener listener;
	
	public TreeTransferHandler(Listener listener) {
		this.listener = listener;
	}
	
    public int getSourceActions(JComponent c) { 
        return COPY_OR_MOVE; 
    } 
    
	protected Transferable createTransferable(JComponent c) { 
    	JTree treeTable = (JTree) c; 
    	DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)treeTable.getSelectionPath().getLastPathComponent();
    	Object userObject = treeNode.getUserObject();
    	if(userObject instanceof Node){
    		return new NodeSelection((Node)userObject);
    	} else {
            return new StringSelection(userObject.toString()); 
    	}

    } 
	
    @Override
    protected void exportDone(JComponent c, Transferable data, int action) {
    	cutAction = action == MOVE;
    } 

    @Override 
    public boolean importData(TransferHandler.TransferSupport info) {
    	try {
	    	if(info.isDrop()){
	    		dropData(info);
	    	} else {
	    		pasteData(info);
	    	}
	    	return true;
    	}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
    } 
    
	private void dropData(TransferSupport info) throws Exception {
		Object data = getImportData(info);
		TreePath path = getDropPath(info);
		Object lastPathComponent = path != null? path.getLastPathComponent() : null;
		listener.onMove((DefaultMutableTreeNode)lastPathComponent, data);
		
	}

	private TreePath getDropPath(TransferSupport info) {
    	JTree.DropLocation dl = (JTree.DropLocation) info.getDropLocation(); 
    	TreePath path = dl.getPath();
		return path;
	}

	private void pasteData(TransferSupport info) throws Exception {
		Object data = getImportData(info);
		JTree tree = getTree(info);
		TreePath path = tree.getSelectionPath();
		Object lastPathComponent = path != null? path.getLastPathComponent() : null;
		if(cutAction){
			listener.onMove((DefaultMutableTreeNode)lastPathComponent, data);
		} else {
			listener.onCopy((DefaultMutableTreeNode)lastPathComponent, data);
		}

		
	}

    private JTree getTree(TransferSupport info) {
		return (JTree) info.getComponent();
	}
    
	private Object getImportData(TransferSupport info) throws Exception {
		Transferable transferable = info.getTransferable();
		if(transferable.isDataFlavorSupported(NODE_FLAVOR)){
			return transferable.getTransferData(NODE_FLAVOR);
		}
		
		if(transferable.isDataFlavorSupported(DataFlavor.stringFlavor)){
			return transferable.getTransferData(DataFlavor.stringFlavor);
		}
		
		return new Object(); //unknow
	}


    @Override
    public boolean canImport(TransferHandler.TransferSupport info) { 
    	boolean canInport = false;
    	Transferable transferable = info.getTransferable();
    	DataFlavor[] transferDataFlavors = transferable.getTransferDataFlavors();
    	if(transferDataFlavors != null){
    		for(DataFlavor dataFlavor : transferDataFlavors){
    			if(dataFlavor.equals(DataFlavor.stringFlavor)){
    				canInport = true;
    				break;
    			}
    			
    			if(dataFlavor.equals(NODE_FLAVOR)){
    				canInport = true;
    				break;
    			}
    		}
    	}
    	info.setShowDropLocation(canInport);
        return canInport; 
    }

}
