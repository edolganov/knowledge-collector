package ru.kc.module.texteditor.tools;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import javax.swing.JEditorPane;

import jsyntaxpane.DefaultSyntaxKit;
import ru.kc.common.controller.Controller;
import ru.kc.common.node.NodeContainer;
import ru.kc.common.node.NodeContainerListener;
import ru.kc.model.Text;
import ru.kc.module.texteditor.ui.TextEditor;
import ru.kc.platform.action.facade.ComboBoxFacade;
import ru.kc.platform.annotations.Mapping;

@Mapping(TextEditor.class)
public class ContentTypesController extends Controller<TextEditor> implements NodeContainer<Text>{
	
	private Text node;
	private ComboBoxFacade comboBox;
	private List<String> contentTypes;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void init() {
		contentTypes = Arrays.asList(DefaultSyntaxKit.getContentTypes());
		
		comboBox = actionService.addComboBoxAction();
		comboBox.setValues((List)contentTypes);
		comboBox.selectValue(0);
		comboBox.addListener(new ComboBoxFacade.Listener() {
			
			@Override
			public void onSelected(Object value) {
				if(value instanceof String)
					selectContentType((String)value);
			}
		});
		

	}


	@Override
	public void setNode(Text node) {
		this.node = node;
		refreshContentType();
	}

	@Override
	public Text getNode() {
		return node;
	}

	private void refreshContentType() {
		
	}
	
	private void selectContentType(String value) {
		if(contentTypes.contains(value)){
			changeContentType(value);
		}
	}
	
	
	private void changeContentType(String type) {

        JEditorPane editor = ui.editor;
		String oldText = editor.getText();
		editor.setContentType(type);

        try {
        	editor.read(new StringReader(oldText), type);
        } catch (Exception ex) {
        	log.error(ex);
        }
	}


	@Override
	public void addListener(NodeContainerListener listener) {}

}
