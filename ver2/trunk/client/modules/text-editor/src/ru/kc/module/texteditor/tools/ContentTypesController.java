package ru.kc.module.texteditor.tools;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.JEditorPane;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import ru.kc.common.controller.Controller;
import ru.kc.common.node.NodeContainer;
import ru.kc.common.node.NodeContainerListener;
import ru.kc.common.node.command.UpdateNode;
import ru.kc.model.Text;
import ru.kc.module.texteditor.TextEditorController;
import ru.kc.module.texteditor.ui.TextEditor;
import ru.kc.platform.action.facade.ComboBoxFacade;
import ru.kc.platform.annotations.Dependence;
import ru.kc.platform.annotations.Mapping;
import ru.kc.tools.filepersist.update.SetProperty;
import ru.kc.util.swing.text.TextComponentUtil;

@Dependence(TextEditorController.class)
@Mapping(TextEditor.class)
public class ContentTypesController extends Controller<TextEditor> implements NodeContainer<Text>{
	
	private static final String TEXT_CONTENT_TYPE = "text.content-type";
	private Text node;
	private ComboBoxFacade comboBox;
	private LinkedHashMap<String,String> contentTypes;
	private String skipSave;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void init() {
		initContentTypes();
		
		comboBox = actionService.addComboBoxAction();
		comboBox.setValues(new ArrayList(contentTypes.keySet()));
		comboBox.addListener(new ComboBoxFacade.Listener() {
			
			@Override
			public void onSelected(Object value) {
				if(value instanceof String){
					String key = (String)value;
					changeContentTypeByKey(key);
					saveContentType(key);
				}
			}
		});
		comboBox.setOrder(100);
		comboBox.setToolTipText("Content type");
	}


	private void initContentTypes() {
		contentTypes = new LinkedHashMap<String,String>();
		contentTypes.put("plain text", SyntaxConstants.SYNTAX_STYLE_NONE);
		contentTypes.put("java", SyntaxConstants.SYNTAX_STYLE_JAVA);
		contentTypes.put("xml", SyntaxConstants.SYNTAX_STYLE_XML);
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
		String key = node.getProperty(TEXT_CONTENT_TYPE);
		if(key == null)
			key = "plain text";
		skipSave = key;
		selectContentTypeByKey(key);
	}
	
	private void selectContentTypeByKey(String key){
		int index = 0;
		for(String candidat : contentTypes.keySet()){
			if(candidat.equals(key)){
				comboBox.selectValue(index);
				return;
			}
			index++;
		}
	}
	
	private void saveContentType(String key) {
		if(!key.equals(skipSave)){
			if(contentTypes.containsKey(key)){
				invokeSafe(new UpdateNode(node, new SetProperty(TEXT_CONTENT_TYPE, key)));
			}
		} else {
			log.info("skip saving content type: "+key);
		}
		skipSave = null;
	}
	

	
	private void changeContentTypeByKey(String key) {
		if(contentTypes.containsKey(key)){
			changeContentType(contentTypes.get(key));
		}
	}
	
	
	private void changeContentType(String type) {
		RSyntaxTextArea editor = ui.editor;
		editor.setSyntaxEditingStyle(type);
	}


	@Override
	public void addListener(NodeContainerListener listener) {}

}
