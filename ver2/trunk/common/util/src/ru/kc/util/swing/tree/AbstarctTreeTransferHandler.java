package ru.kc.util.swing.tree;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;


@SuppressWarnings("serial")
public abstract class AbstarctTreeTransferHandler extends TransferHandler {
	
	public static interface Listener {
		
		void onCopy(DefaultMutableTreeNode destination, Object ob);
		
		void onMove(DefaultMutableTreeNode destination, Object ob);
	}
	
	private boolean cutAction;
	private Listener listener;
	
	public AbstarctTreeTransferHandler(Listener listener) {
		this.listener = listener;
	}
	
    public int getSourceActions(JComponent c) { 
        return COPY_OR_MOVE; 
    } 
    
	protected Transferable createTransferable(JComponent c) { 
    	JTree treeTable = (JTree) c; 
    	DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)treeTable.getSelectionPath().getLastPathComponent();
    	return createTransferable(treeNode);
    }
	
	protected abstract Transferable createTransferable(DefaultMutableTreeNode treeNode);
	
	
	
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
    
	protected abstract Object getImportData(TransferSupport info) throws Exception;
	

    @Override
    public boolean canImport(TransferHandler.TransferSupport info) { 
    	boolean canImport = false;
    	Transferable transferable = info.getTransferable();
    	DataFlavor[] transferDataFlavors = transferable.getTransferDataFlavors();
    	if(transferDataFlavors != null){
    		canImport = canImport(transferDataFlavors);
    	}
    	info.setShowDropLocation(canImport);
        return canImport; 
    }
    
    protected abstract boolean canImport(DataFlavor[] transferDataFlavors);

}
