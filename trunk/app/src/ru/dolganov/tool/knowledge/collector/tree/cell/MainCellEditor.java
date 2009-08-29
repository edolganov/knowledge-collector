package ru.dolganov.tool.knowledge.collector.tree.cell;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.EventObject;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

public class MainCellEditor implements TreeCellEditor, HasCellConst {
	
	NodeButtonsPanel nodeButtons = new NodeButtonsPanel();
	boolean firstClick;
	
	public MainCellEditor() {
		nodeButtons.setBackground(UIManager.getColor("Tree.textBackground"));
		
		nodeButtons.dirB.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e) {
				if(firstClick) {
					
				}
				System.out.println("----dirB!--mouseEntered");
			}
		});
		nodeButtons.dirB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("----dirB!");
			}
			
		});
		
		
		nodeButtons.linkB.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e) {
				System.out.println("----linkB!--mouseEntered");
			}
		});
		nodeButtons.linkB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("----linkB!");
			}
			
		});
		nodeButtons.linkB.addFocusListener(new FocusAdapter(){
			@Override
			public void focusGained(FocusEvent e) {
				System.out.println("----linkB!--focusGained");
			}
		});
		
		nodeButtons.noteB.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseEntered(MouseEvent e) {
				System.out.println("----noteB!--mouseEntered");
			}
		});
		nodeButtons.noteB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("----noteB!");
			}
			
		});
	}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean isSelected, boolean expanded, boolean leaf, int row) {
		Object obj = ((DefaultMutableTreeNode)value).getUserObject();
		firstClick = false;
		if(Cell.BUTTONS == obj){
			return nodeButtons;
		}
		return new JLabel("test");
	}

	@Override
	public void addCellEditorListener(CellEditorListener l) {
		System.out.println("addCellEditorListener");
		
	}

	@Override
	public void cancelCellEditing() {
		System.out.println("cancelCellEditing");
		
	}

	@Override
	public Object getCellEditorValue() {
		System.out.println("getCellEditorValue");
		return null;
	}

	@Override
	public boolean isCellEditable(EventObject anEvent) {
		return needEdit(anEvent);
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l) {
		System.out.println("removeCellEditorListener");
		
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}

	@Override
	public boolean stopCellEditing() {
		System.out.println("stopCellEditing");
		return false;
	}
	
	private boolean needEdit(EventObject anEvent){
		try {
			JTree tree = (JTree)anEvent.getSource();
			Point me = tree.getMousePosition();
			TreePath path = tree.getPathForLocation(me.x, me.y);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();

			Object obj = node.getUserObject();
			if(obj == null) return false;
			if(obj instanceof Cell) return true;
			
			return false;
		} catch (RuntimeException e) {
			e.printStackTrace();
			return false;
		}
	}

}
