package ru.chapaj.util.swing.tree;


import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.Point;
import java.io.IOException;
import java.util.LinkedList;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.*;

/** Creates a JTree with Drag and Drop facilities.
* <p>
* Create and use an object of DNDTree instead of a JTree to include Drag and Drop features for your tree.
* @version 1.01 03/01/2001
* @author Prathap G
*/
public class DNDTree extends JTree implements DropTargetListener,DragSourceListener, DragGestureListener{
	
	public static interface DNDListener {
		public void afterDrop(DefaultMutableTreeNode tagretNode, DefaultMutableTreeNode draggedNode);
	}
	
	
	/** The Drop position. */
	private DropTarget dropTarget = null;
	/** The Drag node.*/
	private DragSource dragSource = null;
	/** The dragged node.*/
	private DefaultMutableTreeNode selnode=null;
	/** The droppped node.*/
	private DefaultMutableTreeNode dropnode=null;
	/** The TreeModel for the tree.*/
	private DefaultTreeModel treemodel=null;
	
	private boolean autoUpdateModel;
	private LinkedList<DNDListener> listeners = new LinkedList<DNDListener>();

	/** Returns a new instance of the DNDTree for the specified TreeModel.*/
	
	public DNDTree(){
		this(false);
	}
	
	public DNDTree(boolean autoUpdateModel){
		this(getDefaultTreeModel(),autoUpdateModel);
	}
	
	public DNDTree(TreeModel model, boolean autoUpdateModel){
		super(model);
		treemodel=(DefaultTreeModel)model;
		dropTarget = new DropTarget (this, this);
		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer( this, DnDConstants.ACTION_MOVE, this);
		this.autoUpdateModel = autoUpdateModel;
	}

	/** Internally implemented, Do not override!*/
	public void dragEnter(DropTargetDragEvent event){
		event.acceptDrag (DnDConstants.ACTION_MOVE);
	}

	/** Internally implemented, Do not override!*/
	public void dragExit(DropTargetEvent event){
	}

	/** Internally implemented, Do not override!*/
	public void dragOver(DropTargetDragEvent event){
	}

	/** Internally implemented, Do not override!*/
	public void drop(DropTargetDropEvent event){
		try {
			Transferable transferable = event.getTransferable();

			if (transferable.isDataFlavorSupported (DataFlavor.stringFlavor)){
				event.acceptDrop(DnDConstants.ACTION_MOVE);
				String s = (String)transferable.getTransferData ( DataFlavor.stringFlavor);
				Object occur=event.getSource();
				Point droppoint=event.getLocation();
				TreePath droppath=getClosestPathForLocation(droppoint.x,droppoint.y);
				dropnode=(DefaultMutableTreeNode)droppath.getLastPathComponent();
				event.getDropTargetContext().dropComplete(true);
			}
			else{
				event.rejectDrop();
			}
		}
		catch (IOException exception) {
			event.rejectDrop();
		}
		catch (UnsupportedFlavorException ufException ) {
			event.rejectDrop();
		}
	}

	/** Internally implemented, Do not override!*/
	public void dropActionChanged ( DropTargetDragEvent event ){
	}

	/** Internally implemented, Do not override!*/
	public void dragGestureRecognized( DragGestureEvent event){
		selnode=null;
		dropnode=null;
		Point dragOrigin = event.getDragOrigin();
		int x = dragOrigin.x;
		int y = dragOrigin.y;
		int selRow = getRowForLocation(x, y);
		if (selRow>=0) {
			TreePath selPath = getPathForLocation(x, y);
			if(selPath != null) {
				setSelectionPath(selPath);
				selnode=(DefaultMutableTreeNode)selPath.getLastPathComponent();
				StringSelection text = new StringSelection( selPath.toString());
				dragSource.startDrag (event, DragSource.DefaultMoveDrop, text, this);
			}
		}
//		Object selected =getSelectionPath();
//		if(selected == null) return;
//		TreePath treepath=(TreePath)selected;
//		selnode=(DefaultMutableTreeNode)treepath.getLastPathComponent();
//		if ( selected != null ){
//			StringSelection text = new StringSelection( selected.toString());
//			dragSource.startDrag (event, DragSource.DefaultMoveDrop, text, this);
//		}
//		else{
//		}
	}

	/** Internally implemented, Do not override!.
	* throws IllegalArgumentException.
	*/
	public void dragDropEnd (DragSourceDropEvent event){
		if ( event.getDropSuccess()){
			if(autoUpdateModel){
				try{
					if(dropnode.equals(selnode)){
						throw new IllegalArgumentException("the source is the same as the destination");
					}
					else{
							dropnode.add(selnode);
					}
				} catch(IllegalArgumentException iae){
					throw new IllegalArgumentException(iae.toString());
				}
				treemodel.reload();
			}
			else {
				try{
					for(DNDListener l : listeners) l.afterDrop(dropnode, selnode);
				}
				finally {
					selnode=null;
					dropnode=null;
				}
			}
        }
	}

	/** Internally implemented, Do not override!*/
	public void dragEnter (DragSourceDragEvent event){
	}

	/** Internally implemented, Do not override!*/
	public void dragExit (DragSourceEvent event){
	}

	/** Internally implemented, Do not override!*/
	public void dragOver (DragSourceDragEvent event){
	}

	/** Internally implemented, Do not override!*/
	public void dropActionChanged ( DragSourceDragEvent event){
	}

	public void addDNDListener(DNDListener listener) {
		listeners.add(listener);
	}
}



