package ru.kc.module.tree.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import ru.kc.common.controller.Controller;
import ru.kc.common.node.command.CreateDirRequest;
import ru.kc.common.node.command.CreateFileLinkRequest;
import ru.kc.common.node.command.CreateLinkRequest;
import ru.kc.common.node.command.CreateTextRequest;
import ru.kc.common.node.command.DeleteNode;
import ru.kc.common.node.command.MoveDown;
import ru.kc.common.node.command.MoveUp;
import ru.kc.model.Node;
import ru.kc.module.tree.ui.Tree;
import ru.kc.platform.action.facade.ButtonFacade;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.common.event.AppInited;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.util.swing.icon.IconUtil;
import ru.kc.util.swing.tree.TreeFacade;

@Mapping(Tree.class)
public class ExportActionsController extends Controller<Tree> {
	
	
	private static final String EDIT_GROUP = "1-edit";
	private static final String ADD_GROUP = "2-add";
	
	TreeFacade treeFacade;
	//basics
	ButtonFacade createDir;
	ButtonFacade createLink;
	ButtonFacade createText;
	ButtonFacade createFileLink;
	//special
	ButtonFacade moveUp;
	ButtonFacade moveDown;
	ButtonFacade delete;
	
	@Override
	protected void init() {
		
		treeFacade = new TreeFacade(ui.tree);
		ui.tree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				refreshActions();
			}
			
		});
		
		initActions();
	}
	
	@EventListener
	public void onAppInited(AppInited event){
		refreshActions();
	}

	
	private void initActions() {
		createDir = actionService.addButtonAction();
		createDir.setIcon(IconUtil.get("/ru/kc/common/img/createDir.png"));
		createDir.setToolTipText("Create dir");
		createDir.setGroup(ADD_GROUP);
		createDir.addListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Node parent = treeFacade.getCurrentObject(Node.class);
				invokeSafe(new CreateDirRequest(parent));
			}
		});
		
		createLink = actionService.addButtonAction();
		createLink.setIcon(IconUtil.get("/ru/kc/common/img/createLink.png"));
		createLink.setToolTipText("Create link");
		createLink.setGroup(ADD_GROUP);
		createLink.addListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Node parent = treeFacade.getCurrentObject(Node.class);
				invokeSafe(new CreateLinkRequest(parent));
			}
		});
		
		createText = actionService.addButtonAction();
		createText.setIcon(IconUtil.get("/ru/kc/common/img/createText.png"));
		createText.setToolTipText("Create text");
		createText.setGroup(ADD_GROUP);
		createText.addListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Node parent = treeFacade.getCurrentObject(Node.class);
				invokeSafe(new CreateTextRequest(parent));
			}
		});
		
		createFileLink = actionService.addButtonAction();
		createFileLink.setIcon(IconUtil.get("/ru/kc/common/img/createFileLink.png"));
		createFileLink.setToolTipText("Create file link");
		createFileLink.setGroup(ADD_GROUP);
		createFileLink.addListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Node parent = treeFacade.getCurrentObject(Node.class);
				invokeSafe(new CreateFileLinkRequest(parent));
			}
		});
		
		
		moveUp = actionService.addButtonAction();
		moveUp.setIcon(IconUtil.get("/ru/kc/common/img/arrow_up.png"));
		moveUp.setToolTipText("Move up  (Ctrl+UP)");
		moveUp.setGroup(EDIT_GROUP);
		moveUp.addListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Node node = treeFacade.getCurrentObject(Node.class);
				if(node != null){
					invokeSafe(new MoveUp(node));
					moveUp.requestFocus();
				}
			}
		});
		
		moveDown = actionService.addButtonAction();
		moveDown.setIcon(IconUtil.get("/ru/kc/common/img/arrow_down.png"));
		moveDown.setToolTipText("Move down  (Ctrl+DOWN)");
		moveDown.setGroup(EDIT_GROUP);
		moveDown.addListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Node node = treeFacade.getCurrentObject(Node.class);
				if(node != null){
					invokeSafe(new MoveDown(node));
					moveDown.requestFocus();
				}
			}
		});
		
		delete = actionService.addButtonAction();
		delete.setIcon(IconUtil.get("/ru/kc/common/img/delete.png"));
		delete.setToolTipText("Delete  (Delete)");
		delete.setGroup(EDIT_GROUP);
		delete.addListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Node node = treeFacade.getCurrentObject(Node.class);
				if(node != null){
					invokeSafe(new DeleteNode(node));
				}
			}
		});
	}


	protected void refreshActions() {
		Node cur = treeFacade.getCurrentObject(Node.class);
		if(cur == null) dissableAll();
		else {
			enableBasics();
			refreshSpecialActions();
		}
		
		
	}

	private void dissableAll() {
		createDir.disable();
		createLink.disable();
		createText.disable();
		createFileLink.disable();
		
		moveUp.disable();
		moveDown.disable();
		delete.disable();
	}

	private void enableBasics() {
		createDir.enabledRequest();
		createLink.enabledRequest();
		createText.enabledRequest();
		createFileLink.enabledRequest();
		
	}
	
	private void refreshSpecialActions() {
		DefaultMutableTreeNode currentTreeNode = treeFacade.getCurrentNode();
		//Node node = treeFacade.getCurrentObject(Node.class);
		
		if(currentTreeNode.isRoot()){
			moveUp.disable();
			moveDown.disable();
			delete.disable();
		} else {
			moveUp.enabledRequest();
			moveDown.enabledRequest();
			delete.enabledRequest();
		}
		
	}



}
