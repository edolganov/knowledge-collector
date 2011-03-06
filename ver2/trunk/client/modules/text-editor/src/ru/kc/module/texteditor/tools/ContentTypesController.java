package ru.kc.module.texteditor.tools;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JEditorPane;

import ru.kc.common.controller.Controller;
import ru.kc.common.node.NodeContainer;
import ru.kc.common.node.NodeContainerListener;
import ru.kc.model.Text;
import ru.kc.module.texteditor.TextEditorController;
import ru.kc.module.texteditor.ui.TextEditor;
import ru.kc.platform.action.facade.ComboBoxFacade;
import ru.kc.platform.annotations.Dependence;
import ru.kc.platform.annotations.Mapping;

@Dependence(TextEditorController.class)
@Mapping(TextEditor.class)
public class ContentTypesController extends Controller<TextEditor> implements NodeContainer<Text>{
	
	private Text node;
	private ComboBoxFacade comboBox;
	private Map<String,String> contentTypes;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void init() {
		initContentTypes();
		
		comboBox = actionService.addComboBoxAction();
		comboBox.setValues(new ArrayList(contentTypes.keySet()));
		comboBox.addListener(new ComboBoxFacade.Listener() {
			
			@Override
			public void onSelected(Object value) {
				if(value instanceof String)
					selectContentType((String)value);
			}
		});
		comboBox.selectValue(0);
		comboBox.setOrder(100);

	}
	
	@Override
	protected void afterAllInited() {
		selectContentType("plain text");
	}


	private void initContentTypes() {
		//contentTypes = Arrays.asList(DefaultSyntaxKit.getContentTypes());
		contentTypes = new LinkedHashMap<String,String>();
		contentTypes.put("plain text","text/plain");
		contentTypes.put("java","text/java");
		contentTypes.put("xml","text/xml");
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
		if(contentTypes.containsKey(value)){
			changeContentType(contentTypes.get(value));
		}
	}
	
	
	private void changeContentType(String type) {

        JEditorPane editor = ui.editor;
		String oldText = editor.getText();
		editor.setContentType(type);

        try {
        	editor.read(new StringReader(oldText), type);
        	getController(TextEditorController.class).reloadEditorListeners();
        } catch (Exception ex) {
        	log.error(ex);
        }
	}


	@Override
	public void addListener(NodeContainerListener listener) {}

}
