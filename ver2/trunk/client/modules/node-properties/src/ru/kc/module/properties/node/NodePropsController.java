package ru.kc.module.properties.node;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ru.kc.common.controller.Controller;
import ru.kc.common.node.command.UpdateNode;
import ru.kc.common.node.edit.NodeEditions;
import ru.kc.common.node.edit.NodeEditionsAggregator;
import ru.kc.common.node.edit.event.DescriptionChanged;
import ru.kc.common.node.edit.event.DescriptionReverted;
import ru.kc.common.node.edit.event.NameChanged;
import ru.kc.common.node.edit.event.NameReverted;
import ru.kc.common.node.edit.event.NodeChanged;
import ru.kc.common.node.edit.event.NodeReverted;
import ru.kc.model.Node;
import ru.kc.module.properties.PropsUpdater;
import ru.kc.module.properties.tools.EmptyTextAreaDecorator;
import ru.kc.module.properties.ui.NodeProps;
import ru.kc.platform.annotations.Mapping;
import ru.kc.platform.event.Event;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.tools.filepersist.update.UpdateDescription;
import ru.kc.tools.filepersist.update.UpdateName;
import ru.kc.util.Check;

@Mapping(NodeProps.class)
public class NodePropsController extends Controller<NodeProps> implements PropsUpdater {

	EmptyTextAreaDecorator desctiptionDecorator;
	boolean enabledUpdateMode = false;
	Node node;
	
	NodeEditionsAggregator nodeEditionsAggregator;
	
	@Override
	protected void init() {
		nodeEditionsAggregator = context.nodeEditionsAggregator;
		
		desctiptionDecorator = new EmptyTextAreaDecorator(ui.description);
		ui.name.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				nameChanged();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				nameChanged();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				nameChanged();
			}
		});
		
		ui.description.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				decriptionChanged();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				decriptionChanged();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				decriptionChanged();
			}
		});
		
		ui.revert.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				revert();
				
			}
		});
		
		ui.save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
	}


	@Override
	public void enableUpdateMode() {
		enabledUpdateMode = true;
	}

	@Override
	public void disableUpdateMode() {
		node = null;
		enabledUpdateMode = false;
	}

	public void setNode(Node node){
		this.node = node;
		fillData();
	}
	
	
	
	private void fillData() {
		boolean oldValue = enabledUpdateMode;
		enabledUpdateMode = false;
		
		NodeEditions editions = nodeEditionsAggregator.getEditions(node);
		String name = editions.get(UpdateName.class);
		String description = editions.get(UpdateDescription.class);
		
		ui.name.setText(name == null? node.getName() : name);
		ui.description.setText(description == null? node.getDescription() : description);
		desctiptionDecorator.fillBackground();
		
		boolean changedData = name != null || description != null;
		setButtonsEnabled(changedData);
		checkSaveButton();
		
		enabledUpdateMode = oldValue;
		
	}

	private void nameChanged() {
		if(!enabledUpdateMode) return;
		
		setButtonsEnabled(true);
		checkSaveButton();
		String newName = ui.name.getText();
		fireEventInEDT(new NameChanged(node, newName));
	}


	private void decriptionChanged() {
		if(!enabledUpdateMode) return;
		
		setButtonsEnabled(true);
		checkSaveButton();
		String description = ui.description.getText();
		fireEventInEDT(new DescriptionChanged(node, description));
	}
	
	private void setButtonsEnabled(boolean value){
		ui.save.setEnabled(value);
		ui.revert.setEnabled(value);
	}
	
	private void checkSaveButton() {
		String newName = ui.name.getText();
		if(Check.isEmpty(newName)){
			ui.save.setEnabled(false);
		}
	}

	private void revert() {
		if(!enabledUpdateMode) return;
		
		NodeEditions editions = nodeEditionsAggregator.getEditions(node);
		editions.remove(UpdateName.class);
		fireEventInEDT(new NameReverted(node));
		editions.remove(UpdateDescription.class);
		fireEventInEDT(new DescriptionReverted(node));
		
		ui.name.requestFocus();
		
		fillData();
	}
	
	private void save() {
		if(!enabledUpdateMode) return;
		
		String newName = ui.name.getText();
		String newDesctiption = ui.description.getText();
		invokeSafe(new UpdateNode(node, 
				new UpdateName(newName), 
				new UpdateDescription(newDesctiption)));
		ui.name.requestFocus();
		
		fillData();
		
	}
	
	@EventListener(NodeChanged.class)
	public void onNodeChanged(NodeChanged event){
		processNodeEvent(event, event.node);
	}
	
	@EventListener(NodeReverted.class)
	public void onNodeChanged(NodeReverted event){
		processNodeEvent(event, event.node);
	}
	
	private void processNodeEvent(Event event, Node eventNode){
		if(!enabledUpdateMode) return;
		if(event.getSender() == this) return;
		if(!eventNode.equals(node)) return;
		
		setNode(eventNode);
	}



	
	
	

}
