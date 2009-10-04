package ru.dolganov.tool.knowledge.collector.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.tree.DefaultMutableTreeNode;

import model.knowledge.Dir;
import model.knowledge.TextData;
import ru.chapaj.util.swing.IconHelper;
import ru.chapaj.util.swing.tree.TreeNodeAdapter;
import ru.dolganov.tool.knowledge.collector.Controller;
import ru.dolganov.tool.knowledge.collector.actions.Actions;
import ru.dolganov.tool.knowledge.collector.annotation.ControllerInfo;
import ru.dolganov.tool.knowledge.collector.tree.dialog.DialogOps;

@ControllerInfo(target=MainWindow.class)
public class MainController extends Controller<MainWindow> {
	
	MainWindow ui;

	@Override
	public void init(final MainWindow ui) {
		this.ui = ui;
		//CRUD in tree
		ui.dirB.setIcon(IconHelper.get("/images/kc/app/dir.png"));
		ui.linkB.setIcon(IconHelper.get("/images/kc/app/netLink.png"));
		ui.noteB.setIcon(IconHelper.get("/images/kc/app/note.png"));
		setButtonEnabled(false);
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
					setButtonEnabled(false);
					return;
				}
				Object ob = node.getUserObject();
				if(ob == null){
					setButtonEnabled(false);
					return;
				}
				
				if(ob instanceof Dir) setButtonEnabled(true);
				else if(ob instanceof TextData) setButtonEnabled(true);
				else setButtonEnabled(false);
			}
		});
	}

	protected void setButtonEnabled(boolean visible) {
		ui.dirB.setEnabled(visible);
		ui.linkB.setEnabled(visible);
		ui.noteB.setEnabled(visible);
	}
	
	

}
