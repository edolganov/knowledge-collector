package ru.kc.module.texteditor;

import ru.kc.common.node.NodeContainer;
import ru.kc.model.Text;
import ru.kc.module.texteditor.ui.TextEditor;
import ru.kc.platform.annotations.GlobalMapping;
import ru.kc.platform.module.Module;

@SuppressWarnings("serial")
@GlobalMapping("text-editor")
public class TextEditorModule extends Module<TextEditor> implements NodeContainer<Text>{

	@Override
	protected TextEditor createUI() {
		return new TextEditor();
	}

	@Override
	public void setNode(Text node) {
		getController(TextEditorController.class).setNode(node);
	}

	@Override
	public Text getNode() {
		return getController(TextEditorController.class).getNode();
	}

}
