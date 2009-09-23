package ru.dolganov.tool.knowledge.collector.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;

import model.knowledge.Dir;
import model.knowledge.TextData;

import ru.chapaj.util.swing.IconHelper;
import ru.chapaj.util.swing.tree.TreeNodeAdapter;
import ru.dolganov.tool.knowledge.collector.Controller;
import ru.dolganov.tool.knowledge.collector.actions.Actions;
import ru.dolganov.tool.knowledge.collector.tree.dialog.DialogOps;

public class MainController extends Controller<MainWindow> {
	
	MainWindow ui;

	@Override
	public void init(final MainWindow ui) {
		this.ui = ui;
		
		ui.saveB.addChangeListener(new ChangeListener(){

			@Override
			public void stateChanged(ChangeEvent e) {
				dao.flushMeta();
			}
			
		});
		
		ui.saveB.setEnabled(false);
		
		ui.dirB.setIcon(IconHelper.get("/images/kc/app/dir.png"));
		ui.linkB.setIcon(IconHelper.get("/images/kc/app/netLink.png"));
		ui.noteB.setIcon(IconHelper.get("/images/kc/app/note.png"));
		setButtonVisible(false);
		
		ui.dirB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Actions.addTreeNode(DialogOps.newDir());
			}
			
		});
		
		ui.linkB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Actions.addTreeNode(DialogOps.newLink());
			}
			
		});
		
		ui.noteB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Actions.addTreeNode(DialogOps.newText());
			}
			
		});
		
		ui.tree.addTreeNodeListener(new TreeNodeAdapter(){
			@Override
			public void onNodeSelect(DefaultMutableTreeNode node) {
				if(node == null) {
					setButtonVisible(false);
					return;
				}
				Object ob = node.getUserObject();
				if(ob == null){
					setButtonVisible(false);
					return;
				}
				
				if(ob instanceof Dir) setButtonVisible(true);
				else if(ob instanceof TextData) setButtonVisible(true);
				else setButtonVisible(false);
			}
		});
	}

	protected void setButtonVisible(boolean visible) {
		ui.dirB.setEnabled(visible);
		ui.linkB.setEnabled(visible);
		ui.noteB.setEnabled(visible);
	}
	
	

}
