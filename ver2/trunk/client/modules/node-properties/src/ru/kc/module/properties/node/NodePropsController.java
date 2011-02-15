package ru.kc.module.properties.node;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ru.kc.common.controller.Controller;
import ru.kc.common.node.edit.event.DescriptionChanged;
import ru.kc.common.node.edit.event.NameChanged;
import ru.kc.model.Node;
import ru.kc.module.properties.PropsUpdater;
import ru.kc.module.properties.tools.EmptyTextAreaDecorator;
import ru.kc.module.properties.ui.NodeProps;
import ru.kc.platform.annotations.Mapping;

@Mapping(NodeProps.class)
public class NodePropsController extends Controller<NodeProps> implements PropsUpdater {

	EmptyTextAreaDecorator desctiptionDecorator;
	boolean fireChanges = false;
	Node node;
	
	@Override
	protected void init() {
		desctiptionDecorator = new EmptyTextAreaDecorator(ui.description);
		ui.name.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				fireNameChanged();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				fireNameChanged();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				fireNameChanged();
			}
		});
		
		ui.description.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				fireDecriptionChanged();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				fireDecriptionChanged();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				fireDecriptionChanged();
			}
		});
	}
	
	@Override
	public void enableUpdateMode() {
		fireChanges = true;
	}

	@Override
	public void disableUpdateMode() {
		node = null;
		fireChanges = false;
	}

	public void setNode(Node node){
		boolean oldFireChangeValue = fireChanges;
		fireChanges = false;
		
		ui.name.setText(node.getName());
		ui.description.setText(node.getDescription());
		desctiptionDecorator.fillBackground();
		this.node = node;
		
		fireChanges = oldFireChangeValue;
	}
	
	
	
	protected void fireNameChanged() {
		if(!fireChanges) return;
		
		String newName = ui.name.getText();
		fireEventInEDT(new NameChanged(node, newName));
	}
	
	protected void fireDecriptionChanged() {
		if(!fireChanges) return;
		
		String description = ui.description.getText();
		fireEventInEDT(new DescriptionChanged(node, description));
	}




	
	
	

}
