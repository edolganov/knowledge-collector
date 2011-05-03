package ru.kc.module.texteditor;

import java.util.ArrayList;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import ru.kc.common.controller.Controller;
import ru.kc.common.node.NodeContainer;
import ru.kc.common.node.NodeContainerListener;
import ru.kc.common.node.edit.NodeEditions;
import ru.kc.common.node.edit.NodeEditionsAggregator;
import ru.kc.common.node.edit.event.NodeChanged;
import ru.kc.common.node.edit.event.NodeReverted;
import ru.kc.common.node.edit.event.TextChanged;
import ru.kc.model.Node;
import ru.kc.model.Text;
import ru.kc.module.texteditor.ui.TextEditor;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.event.Event;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.tools.filepersist.update.UpdateText;
import ru.kc.util.swing.text.TextComponentUtil;

@Mapping(TextEditor.class)
public class TextEditorController extends Controller<TextEditor> implements NodeContainer<Text>{
	
	private Text node;
	private RSyntaxTextArea editor;
	private boolean enabledUpdateMode = true;
	private NodeEditionsAggregator nodeEditionsAggregator;
	private ArrayList<NodeContainerListener> listeners = new ArrayList<NodeContainerListener>();
	
	@Override
	protected void init() {
		nodeEditionsAggregator = context.nodeEditionsAggregator;
		editor = ui.editor;
		reloadEditorListeners();
	}
	
	public void reloadEditorListeners() {
		editor.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				textChanged();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				textChanged();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {}
		});
	}
	
	protected void textChanged() {
		if(!enabledUpdateMode) return;
		
		String newText = editor.getText();
		fireEvent(new TextChanged(node, newText));
		for(NodeContainerListener l : listeners) l.onModified(node);
	}

	@Override
	public void setNode(Text node) {
		this.node = node;
		fillData();
	}
	
	@Override
	public Text getNode() {
		return node;
	}

	private void fillData() {
		boolean oldValue = enabledUpdateMode;
		enabledUpdateMode = false;
		
		NodeEditions editions = nodeEditionsAggregator.getEditions(node);
		String text = editions.get(UpdateText.class);
		boolean modified = text != null;
		TextComponentUtil.setTextWithSaveCaretPosition(editor, modified? text : node.safeGetText());

		if(modified){
			for(NodeContainerListener l : listeners) l.onModified(node);
		} else {
			for(NodeContainerListener l : listeners) l.onReverted(node);
		}
		
		enabledUpdateMode = oldValue;
	}


	
	@EventListener
	public void onNodeChanged(NodeChanged event){
		processNodeEvent(event, event.node);
	}

	@EventListener
	public void onNodeReverted(NodeReverted event){
		processNodeEvent(event, event.node);
	}
	
	
	private void processNodeEvent(Event event, Node eventNode){
		if(!enabledUpdateMode) return;
		if(event.getSender() == this) return;
		if(!eventNode.equals(node)) return;
		
		try {
			Text node = (Text) eventNode;
			setNode(node);
		}catch (ClassCastException e) {
			log.error("class cast exception", e);
		}

	}
	

	@Override
	public void addListener(NodeContainerListener listener) {
		listeners.add(listener);
	}


	
}
