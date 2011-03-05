package ru.kc.module.texteditor.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ru.kc.common.controller.Controller;
import ru.kc.common.node.NodeContainer;
import ru.kc.common.node.NodeContainerListener;
import ru.kc.common.node.edit.NodeEditions;
import ru.kc.common.node.edit.event.NodeChanged;
import ru.kc.common.node.edit.event.NodeReverted;
import ru.kc.model.Node;
import ru.kc.model.Text;
import ru.kc.module.texteditor.TextEditorController;
import ru.kc.module.texteditor.ui.TextEditor;
import ru.kc.platform.action.facade.ButtonFacade;
import ru.kc.platform.annotations.Dependence;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.event.Event;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.tools.filepersist.update.UpdateText;
import ru.kc.util.swing.icon.IconUtil;

@Dependence(TextEditorController.class)
@Mapping(TextEditor.class)
public class EditorActionsController extends Controller<TextEditor> implements NodeContainer<Text>{
	
	private Text node;
	private ButtonFacade save;

	@Override
	protected void init() {
		
		
		save = actionService.addButtonAction();
		save.setToolTipText("save");
		save.setIcon(IconUtil.get("/ru/kc/common/img/save.png"));
		save.addListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("!!!");
			}
		});
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
		refreshSave();
	}
	

	private void refreshSave() {
		NodeEditions editions = context.nodeEditionsAggregator.getEditions(node);
		String text = editions.get(UpdateText.class);
		boolean modified = text != null;
		
		if(modified) 
			save.enabledRequest();
		else 
			save.disable();
	}



	@Override
	public void addListener(NodeContainerListener listener) {
		// TODO Auto-generated method stub
		
	}
	
	@EventListener
	public void onNodeChanged(NodeChanged event){
		if(event.node == node)
			refreshSave();
	}

	@EventListener
	public void onNodeReverted(NodeReverted event){
		if(event.node == node)
			refreshSave();
	}
	
	@Override
	protected void onNodeUpdated(Node old, Node updatedNode) {
		if(old == node){
			if(updatedNode instanceof Text){
				setNode((Text)updatedNode);
			}
		}
	}

}
