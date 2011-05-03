package ru.kc.module.texteditor.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ru.kc.common.controller.Controller;
import ru.kc.common.node.NodeContainer;
import ru.kc.common.node.NodeContainerListener;
import ru.kc.common.node.edit.event.NodeChanged;
import ru.kc.common.node.edit.event.NodeReverted;
import ru.kc.model.Text;
import ru.kc.module.texteditor.ui.TextEditor;
import ru.kc.platform.action.facade.AbstractButtonFacade;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.util.swing.icon.IconUtil;

@Mapping(TextEditor.class)
public class WrapLineController extends Controller<TextEditor> implements NodeContainer<Text>{
	
	private Text node;
	private AbstractButtonFacade wrap;

	@Override
	protected void init() {
		
		wrap = actionService.addToggleButtonAction();
		wrap.setToolTipText("Word wrap");
		wrap.setIcon(IconUtil.get("/ru/kc/module/texteditor/img/word-wrap.png"));
		wrap.addListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				wrap(wrap.isSelected());
			}
		});
		wrap.setOrder(3);
		

	}


	protected void wrap(boolean selected) {
		ui.editor.setLineWrap(selected);
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
		refreshButtons();
	}
	

	private void refreshButtons() {
		//nothing
	}


	
	@EventListener
	public void onNodeChanged(NodeChanged event){
		if(event.node == node)
			refreshButtons();
	}

	@EventListener
	public void onNodeReverted(NodeReverted event){
		if(event.node == node)
			refreshButtons();
	}
	
	
	@Override
	public void addListener(NodeContainerListener listener) {}

}
