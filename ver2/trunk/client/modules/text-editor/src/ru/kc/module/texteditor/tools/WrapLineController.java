package ru.kc.module.texteditor.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ru.kc.common.controller.Controller;
import ru.kc.common.node.NodeContainer;
import ru.kc.common.node.NodeContainerListener;
import ru.kc.common.node.command.UpdateNode;
import ru.kc.model.Node;
import ru.kc.model.Text;
import ru.kc.module.texteditor.ui.TextEditor;
import ru.kc.platform.action.facade.AbstractButtonFacade;
import ru.kc.platform.annotations.Mapping;
import ru.kc.tools.filepersist.update.SetProperty;
import ru.kc.util.swing.icon.IconUtil;

@Mapping(TextEditor.class)
public class WrapLineController extends Controller<TextEditor> implements NodeContainer<Text>{
	
	private static final String KEY = "text.word-wrap";
	private Text node;
	private AbstractButtonFacade wrap;
	private boolean oldValue = false;

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


	@Override
	public void setNode(Text node) {
		this.node = node;
		initButton();
	}
	
	private void initButton() {
		boolean value = getValue(node, false);
		oldValue = value;
		wrap.setSelected(value);
		wrap(value);
	}

	protected void wrap(boolean selected) {
		ui.editor.setLineWrap(selected);
		if(selected != oldValue){
			oldValue = selected;
			setValue(selected);
			wrap.setSelected(selected);
		}
	}

	private boolean getValue(Node node, boolean defaultValue){
		try {
			return Boolean.parseBoolean(node.getProperty(KEY));
		}catch (Exception e) {
			return defaultValue;
		}
	}
	
	private void setValue(boolean selected) {
		log.info("save word wrap: "+selected);
		invokeSafe(new UpdateNode(node, new SetProperty(KEY, ""+selected)));
	}

	@Override
	public Text getNode() {
		return node;
	}
	
	@Override
	public void addListener(NodeContainerListener listener) {}

}
