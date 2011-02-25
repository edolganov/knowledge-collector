package ru.kc.module.texteditor;

import javax.swing.JEditorPane;

import ru.kc.common.controller.Controller;
import ru.kc.common.node.NodeContainer;
import ru.kc.model.Text;
import ru.kc.module.texteditor.ui.TextEditor;
import ru.kc.platform.annotations.Mapping;

@Mapping(TextEditor.class)
public class TextEditorController extends Controller<TextEditor> implements NodeContainer<Text>{
	
	private Text node;
	private JEditorPane editor;
	
	@Override
	protected void init() {
		editor = ui.editor;
		editor.setContentType("text/groovy");
		//undoManHack = new CompoundUndoManHack(pane);
	}

	@Override
	public void setNode(Text node) {
		this.node = node;
		setText(node.safeGetText());
	}

	private void setText(String content) {
		editor.setText(content);
	}

	@Override
	public Text getNode() {
		return node;
	}

}
