package ru.kc.module.texteditor;

import ru.kc.common.FocusProvider;
import ru.kc.common.node.NodeContainer;
import ru.kc.common.node.NodeContainerListener;
import ru.kc.model.Text;
import ru.kc.module.texteditor.ui.TextEditor;
import ru.kc.platform.annotations.GlobalMapping;
import ru.kc.platform.controller.AbstractController;
import ru.kc.platform.module.Module;

@SuppressWarnings("serial")
@GlobalMapping("text-editor")
public class TextEditorModule extends Module<TextEditor> implements NodeContainer<Text>, FocusProvider {

	@Override
	protected TextEditor createUI() {
		return new TextEditor();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setNode(Text node) {
		for(AbstractController<?> c : getControllers()){
			if(c instanceof NodeContainer){
				((NodeContainer) c).setNode(node);
			}
		}
	}

	@Override
	public Text getNode() {
		return getController(TextEditorController.class).getNode();
	}

	@Override
	public void addListener(NodeContainerListener listener) {
		getController(TextEditorController.class).addListener(listener);
	}

	@Override
	public void setFocusRequest() {
		getController(TextEditorController.class).setFocusRequest();
	}

}
