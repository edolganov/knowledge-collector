package ru.kc.module.texteditor;

import ru.kc.common.controller.Controller;
import ru.kc.common.node.NodeContainer;
import ru.kc.model.Text;
import ru.kc.module.texteditor.ui.TextEditor;
import ru.kc.platform.annotations.Mapping;

@Mapping(TextEditor.class)
public class TextEditorController extends Controller<TextEditor> implements NodeContainer<Text>{

	private Text node;
	
	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNode(Text node) {
		this.node = node;
	}

	@Override
	public Text getNode() {
		return node;
	}

}
