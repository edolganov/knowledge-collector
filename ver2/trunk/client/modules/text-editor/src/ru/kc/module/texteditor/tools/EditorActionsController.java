package ru.kc.module.texteditor.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import ru.kc.common.controller.Controller;
import ru.kc.common.node.NodeContainer;
import ru.kc.common.node.NodeContainerListener;
import ru.kc.common.node.edit.NodeEditions;
import ru.kc.common.node.edit.event.NodeChanged;
import ru.kc.common.node.edit.event.NodeReverted;
import ru.kc.common.node.edit.event.RevertNodeRequest;
import ru.kc.common.node.edit.event.UpdateNodeRequest;
import ru.kc.model.Text;
import ru.kc.module.texteditor.ui.TextEditor;
import ru.kc.platform.action.facade.ButtonFacade;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.tools.filepersist.update.UpdateText;
import ru.kc.util.swing.icon.IconUtil;

@Mapping(TextEditor.class)
public class EditorActionsController extends Controller<TextEditor> implements NodeContainer<Text>{
	
	private Text node;
	private ButtonFacade save;
	private ButtonFacade revert;

	@Override
	protected void init() {
		
		revert = actionService.addButtonAction();
		revert.setToolTipText("Revert");
		revert.setIcon(IconUtil.get("/ru/kc/common/img/revert.png"));
		revert.addListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				revert();
			}
		});
		
		save = actionService.addButtonAction();
		save.setToolTipText("Save");
		save.setIcon(IconUtil.get("/ru/kc/common/img/save.png"));
		save.addListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				save();  
			}
		});
		
		
		ui.editor.getActionMap().put("save", new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		KeyStroke keyStroke = KeyStroke.getKeyStroke("control S");
		ui.editor.getInputMap().put(keyStroke, "save");

	}

	private void save(){
		fireEventInEDT(new UpdateNodeRequest(node));
	}
	
	private void revert(){
		fireEventInEDT(new RevertNodeRequest(node));
	}


	@Override
	public void setNode(Text node) {
		this.node = node;
		refreshActions();
	}

	@Override
	public Text getNode() {
		return node;
	}
	
	private void refreshActions() {
		refreshSaveAndRevert();
	}
	

	private void refreshSaveAndRevert() {
		NodeEditions editions = context.nodeEditionsAggregator.getEditions(node);
		String text = editions.get(UpdateText.class);
		boolean modified = text != null;
		
		if(modified) {
			save.enabledRequest();
			revert.enabledRequest();
		}
		else {
			save.disable();
			revert.disable();
		}
	}


	
	@EventListener
	public void onNodeChanged(NodeChanged event){
		if(event.node == node)
			refreshSaveAndRevert();
	}

	@EventListener
	public void onNodeReverted(NodeReverted event){
		if(event.node == node)
			refreshSaveAndRevert();
	}
	
	
	@Override
	public void addListener(NodeContainerListener listener) {}

}
