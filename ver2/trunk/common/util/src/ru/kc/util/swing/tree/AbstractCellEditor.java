package ru.kc.util.swing.tree;

import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.plaf.TreeUI;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import ru.kc.util.swing.keyboard.EnterKey;
import ru.kc.util.swing.tree.TreeFacade;

public abstract class AbstractCellEditor implements TreeCellEditor {
	
	protected TreeFacade treeFacade;
	protected CellPanel cellPanel;
	protected JTextField text;
	protected JLabel label;
	private boolean enabledRequest;
	private boolean enabled;
	private ArrayList<CellEditorListener> customListeners = new ArrayList<CellEditorListener>();
	private ArrayList<CellEditorListener> swingListeners = new ArrayList<CellEditorListener>();
	
	public AbstractCellEditor(JTree tree) {
		treeFacade = new TreeFacade(tree);
		initRender();

	}

	private void initRender() {
		cellPanel = new CellPanel();
		cellPanel.setOpaque(false);
		text = cellPanel.textF;
		label = cellPanel.label;
		
		label.setText("");
		text.setText("");
		
		text.addKeyListener(new EnterKey() {
			
			@Override
			protected void doAction(KeyEvent e) {
				stopCellEditing();
			}
		});
		
		text.addFocusListener(new FocusAdapter() {
			
			@Override
			public void focusLost(FocusEvent e) {
				cancelCellEditing();
			}
		});
	}
	
	public void setEnabledRequest() {
		this.enabledRequest = true;
	}

	@Override
	public String getCellEditorValue() {
		return text.getText();
	}

	@Override
	public boolean isCellEditable(EventObject anEvent) {
		return enabledRequest;
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		return enabledRequest;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	@Override
	public void cancelCellEditing() {
		if(!enabled) return;
		
		enabledRequest = false;
		enabled = false;
		
		for (CellEditorListener l : customListeners)l.editingCanceled(new ChangeEvent(this));
		for (CellEditorListener l : swingListeners)l.editingCanceled(new ChangeEvent(this));
	}

	@Override
	public boolean stopCellEditing() {
		boolean needStop = isStopCellEditing();
		if(!needStop)
			return false;
		
		
		enabledRequest = false;
		enabled = false;
		
		for (CellEditorListener l : customListeners)l.editingStopped(new ChangeEvent(this));
		for (CellEditorListener l : swingListeners)l.editingCanceled(new ChangeEvent(this));
		return true;
	}
	
	protected abstract boolean isStopCellEditing();


	public void addCustomListener(CellEditorListener l) {
		customListeners.add(l);
	}

	@Override
	public void addCellEditorListener(CellEditorListener l) {
		swingListeners.add(l);
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l) {
		swingListeners.remove(l);
	}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean isSelected, boolean expanded, boolean leaf, int row) {
		fillCellEditorPanel();
		enabled = true;
		return cellPanel;
	}
	
	protected void fillCellEditorPanel() {
        JTree tree = treeFacade.tree;
		TreeModel model = tree.getModel();
        DefaultMutableTreeNode node = treeFacade.getCurrentNode();
		TreePath path = new TreePath(node.getPath());
        TreeCellRenderer r = tree.getCellRenderer();
        TreeUI ui = tree.getUI();
        int row = ui.getRowForPath(tree, path);
        int lsr = tree.getLeadSelectionRow();
        boolean hasFocus = tree.isFocusOwner() && (lsr == row);
        boolean selected = tree.isPathSelected(path);
        boolean expanded = tree.isExpanded(path);
		boolean leaf = model.isLeaf(node);
		Component component = r.getTreeCellRendererComponent(tree, 
				node, selected, expanded, 
                leaf, row, hasFocus);
        
        if(component instanceof DefaultTreeCellRenderer){
        	DefaultTreeCellRenderer render = (DefaultTreeCellRenderer) component;
    		label.setIcon(render.getIcon());
    		text.setText(render.getText());
        }
        else throw new IllegalStateException("unknow type of tree render: "+r);
	}





}
