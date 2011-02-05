package ru.kc.util.swingx;

import java.awt.*; 
import java.awt.datatransfer.DataFlavor; 
import java.awt.datatransfer.StringSelection; 
import java.awt.datatransfer.Transferable; 
import java.awt.datatransfer.UnsupportedFlavorException; 
import java.awt.geom.Point2D; 
import java.io.IOException; 
import javax.swing.*; 
import org.jdesktop.swingx.JXTable; 
import org.jdesktop.swingx.JXTreeTable; 
import org.jdesktop.swingx.decorator.*; 
import org.jdesktop.swingx.decorator.HighlightPredicate.ColumnHighlightPredicate; 
import org.jdesktop.swingx.painter.CheckerboardPainter; 
import org.jdesktop.swingx.renderer.DefaultTreeRenderer; 
import org.jdesktop.swingx.renderer.IconValue; 
import org.jdesktop.swingx.renderer.StringValue; 
import org.jdesktop.swingx.treetable.*; 

public class JXTreeTableTest extends JFrame {


	    public static void main(String args[]) { 
	        SwingUtilities.invokeLater(new Runnable() { 

	            public void run() { 

	                try { 
	                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
	                } catch (Exception e) { 
	                    //Do nothing 
	                } 

	                JXTreeTable treeTable = new JXTreeTable(); 
	                treeTable.setTreeTableModel(new RandomTextTreeTableModel(5)); 

	                treeTable.setColumnSelectionAllowed(true); 
	                treeTable.setRowSelectionAllowed(true); 
	                treeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 

	                treeTable.setDragEnabled(true); 
	                treeTable.setEditable(false); 
	                treeTable.setTreeCellRenderer(new DefaultTreeRenderer()); 
	                treeTable.setTransferHandler(new TreeTableTransferHandler()); 

	                treeTable.setSelectionBackground(new Color(176, 197, 227)); 
	                treeTable.setSelectionForeground(new Color(0, 0, 128)); 
	                treeTable.setForeground(new Color(0, 0, 128)); 
	                treeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 

	                // Commented out.  below changes highlight behavior 
	                //treeTable.setDropMode(DropMode.INSERT); 
	                //treeTable.setDropMode(DropMode.ON_OR_INSERT_ROWS); 

	                // Add multiple highlighters 
	                BorderHighlighter border = new BorderHighlighter(new ColumnHighlightPredicate(1, 2), 
	                        BorderFactory.createEmptyBorder(0, 6, 0, 6), false, false); 


	                treeTable.addHighlighter(border); 

	                // Add a silly drop painter 
	                GradientPaint gp = new GradientPaint( 
	                        new Point2D.Double(0, 0), 
	                        Color.BLACK, 
	                        new Point2D.Double(0, 4), 
	                        Color.GRAY); 
	                CheckerboardPainter p = new CheckerboardPainter(); 
	                p.setDarkPaint(gp); 
	                p.setLightPaint(Color.WHITE); 
	                p.setSquareSize(4); 
	                PainterHighlighter pp = new PainterHighlighter(new DropHighlightPredicate(), p); 

	                treeTable.addHighlighter(pp); 

	                treeTable.addHighlighter(HighlighterFactory.createAlternateStriping()); 

	                JFrame frame = new JFrame("Testing"); 
	                frame.setLayout(new BorderLayout()); 
	                frame.getContentPane().add(new JScrollPane(treeTable), BorderLayout.CENTER); 
	                frame.setSize(400, 300); 
	                frame.setVisible(true); 
	                frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE); 
	            } 
	        }); 
	    } 
	} 

	class DropHighlightPredicate implements HighlightPredicate { 

	    public boolean isHighlighted(Component renderer, ComponentAdapter adapter) { 
	        Object o = adapter.getComponent(); 
	        int r = adapter.row; 
	        if (o instanceof JTable) { 
	            JTable.DropLocation dl = ((JTable) o).getDropLocation(); 
	            if (dl == null) { 
	                return false; 
	            } 

	            if (dl.getRow() != r) { 
	                System.out.println("Not drop row" + r + " d:" + dl.getRow()); 
	                return false; 
	            } 
	            System.out.println("Drop pred:" + dl.getRow()); 
	            return true; 
	        } 
	        return false; 
	    } 
	} 

	class TreeTableTransferHandler extends TransferHandler { 

	    /** 
	     * Perform the actual data import. 
	     */ 
	    @Override 
	    public boolean importData(TransferHandler.TransferSupport info) { 
	        String data = null; 
	        //If we can't handle the import, bail now. 
	        if (!canImport(info)) { 
	            return false; 
	        } 

	        JXTreeTable treeTable = (JXTreeTable) info.getComponent(); 
	        //Fetch the data -- bail if this fails 
	        try { 
	            data = (String) info.getTransferable().getTransferData(DataFlavor.stringFlavor); 
	        } catch (UnsupportedFlavorException ufe) { 
	            System.out.println("importData: unsupported data flavor"); 
	            return false; 
	        } catch (IOException ioe) { 
	            System.out.println("importData: I/O exception"); 
	            return false; 
	        } 

	        if (info.isDrop()) { //This is a drop 
	            JXTreeTable.DropLocation dl = (JXTreeTable.DropLocation) info.getDropLocation(); 
	            int row = dl.getRow(); 
	            int col = dl.getColumn(); 
	            //int index = dl.getIndex();     
	            if (dl.isInsertRow()) { 
	                //model.add(index, data); 
	                System.out.println("Insert at:" + row + ":" + data); 

	                return true; 
	            } else { 
	                System.out.println("Set at:" + row + ":" + data); 
	                return true; 
	            } 
	        } else { //This is a paste 
	            int row = treeTable.getSelectedRow(); 
	            int col = treeTable.getSelectedColumn(); 

	            System.out.println("Paste at:" + row + ":" + data); 
	            return true; 
	        } 
	    } 

	    /** 
	     * Bundle up the data for export. 
	     */ 
	    protected Transferable createTransferable(JComponent c) { 
	        JXTreeTable treeTable = (JXTreeTable) c; 
	        int row = treeTable.getSelectedRow(); 
	        int col = treeTable.getSelectedColumn(); 
	        String value = treeTable.getValueAt(row, col).toString(); 
	        System.out.println("create at:" + row + ":" + value); 
	        return new StringSelection(value); 
	    } 

	    /** 
	     * The treeTable handles copy actions. 
	     */ 
	    public int getSourceActions(JComponent c) { 
	        return COPY; 
	    } 

	    /** 
	     * When the export is complete, remove the old treeTable entry if the 
	     * action was a move. 
	     */ 
	    protected void exportDone(JComponent c, Transferable data, int action) { 
	        if (action != MOVE) { 
	            return; 
	        } 
	        JXTreeTable treeTable = (JXTreeTable) c; 
	        //int index = treeTable.getSelectedIndex(); 
	        int row = treeTable.getSelectedRow(); 
	        int col = treeTable.getSelectedColumn(); 
	        System.out.println("Export done at:" + row); 
	    //model.remove(index); 
	    } 

	    /** 
	     * 
	     */ 
	    public boolean canImport(TransferHandler.TransferSupport support) { 
	        JXTreeTable.DropLocation dl = (JXTreeTable.DropLocation) support.getDropLocation(); 
	        int row = dl.getRow(); 
	        int col = dl.getColumn(); 

	        support.setShowDropLocation(false); 
	        if (row < 0) { 
	            return false;        // Only allow drop on column 0 
	        } 
	        if (col != 0) { 
	            return false; 
	        } 

	        // we accept Strings 
	        if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) { 
	            return false; 
	        } 

	        support.setShowDropLocation(true); 
	        return true; 
	    } 
	} 

	class RandomTextTreeTableModel extends DefaultTreeTableModel { 

	    DefaultMutableTreeTableNode Root = null; 

	    RandomTextTreeTableModel(int i) { 
	        Root = new DefaultMutableTreeTableNode(); 
	        this.setRoot(Root); 
	        for (int x = 0; x < 5; x++) { 
	            DefaultMutableTreeTableNode node = new DefaultMutableTreeTableNode("HI"); 
	            Root.add(node); 
	            for (int j = 0; j < i; j++) { 
	                node.add(new DefaultMutableTreeTableNode(j)); 
	            } 
	        } 
	    } 

	    @Override 
	    public int getColumnCount() { 
	        return 2; 
	    } 

	    @Override 
	    public Object getValueAt(Object arg0, int arg1) { 
	        if (arg1 == 1) { 
	            return new String("hi " + arg0.toString()); 
	        } 
	        return arg0; 
	    } 
	}
