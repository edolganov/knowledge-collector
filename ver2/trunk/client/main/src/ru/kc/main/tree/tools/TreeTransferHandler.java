package ru.kc.main.tree.tools;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;

public class TreeTransferHandler extends TransferHandler {
	

    @Override 
    public boolean importData(TransferHandler.TransferSupport info) { 
        JTree tree = (JTree) info.getComponent(); 
        String data = null; 
    	
        try { 
            data = (String) info.getTransferable().getTransferData(DataFlavor.stringFlavor); 
        } catch (UnsupportedFlavorException ufe) { 
            System.out.println("importData: unsupported data flavor"); 
            return false; 
        } catch (IOException ioe) { 
            System.out.println("importData: I/O exception"); 
            return false; 
        } 

        if (info.isDrop()) {
        	JTree.DropLocation dl = (JTree.DropLocation) info.getDropLocation(); 
        	TreePath path = dl.getPath();
            System.out.println("2 set " + path); 
            return true; 
             
        } else {
            System.out.println("2 paste " + tree.getSelectionPath()); 
            return true; 
        } 
    } 

    protected Transferable createTransferable(JComponent c) { 
    	JTree treeTable = (JTree) c; 
    	Object ob = treeTable.getSelectionPath().getLastPathComponent();
    	System.out.println("1 try to drug " + ob);
        return new StringSelection(ob.toString()); 
    } 

    public int getSourceActions(JComponent c) { 
        return COPY_OR_MOVE; 
    } 

    @Override
    protected void exportDone(JComponent c, Transferable data, int action) { 
        JTree tree = (JTree) c; 
        TreePath path = tree.getSelectionPath();
        System.out.println("3 export done" + path); 
    } 

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) { 
    	support.setShowDropLocation(true);
        return true; 
    } 

}
