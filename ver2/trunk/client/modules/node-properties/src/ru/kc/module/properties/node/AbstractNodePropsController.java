package ru.kc.module.properties.node;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

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
import ru.kc.platform.event.Event;
import ru.kc.platform.event.annotation.EventListener;
import ru.kc.tools.filepersist.update.UpdateDescription;
import ru.kc.tools.filepersist.update.UpdateName;
import ru.kc.tools.filepersist.update.UpdateRequest;
import ru.kc.util.Check;

public abstract class AbstractNodePropsController<N extends Node, T> extends Controller<T> implements PropsUpdater {

	protected JTextComponent name;
	protected JTextComponent description;
	protected JButton save;
	protected JButton revert;
	
	protected EmptyTextAreaDecorator desctiptionDecorator;
	protected boolean enabledUpdateMode = false;
	protected N node;
	
	protected NodeEditionsAggregator nodeEditionsAggregator;
	
	protected void init(JTextComponent name, JTextComponent description, JButton save, JButton revert) {
		nodeEditionsAggregator = context.nodeEditionsAggregator;
		this.name = name;
		this.description = description;
		this.save = save;
		this.revert = revert;
		desctiptionDecorator = new EmptyTextAreaDecorator(description);
		
		name.getDocument().addDocumentListener(new DocumentListener() {
			
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
		
		description.getDocument().addDocumentListener(new DocumentListener() {
			
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
		
		revert.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				revert();
				
			}
		});
		
		save.addActionListener(new ActionListener() {
			
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

	public void setNode(N node){
		this.node = node;
		fillData();
	}
	
	
	
	private void fillData() {
		boolean oldValue = enabledUpdateMode;
		enabledUpdateMode = false;
		
		NodeEditions editions = nodeEditionsAggregator.getEditions(node);
		String name = editions.get(UpdateName.class);
		String description = editions.get(UpdateDescription.class);
		
		this.name.setText(name == null? node.getName() : name);
		this.name.setCaretPosition(0);
		
		this.description.setText(description == null? node.getDescription() : description);
		this.description.setCaretPosition(0);
		desctiptionDecorator.fillBackground();
		
		boolean changedData = name != null || description != null;
		setButtonsEnabled(changedData);
		
		extendedFillData(editions);

		enabledUpdateMode = oldValue;
	}
	
	protected void extendedFillData(NodeEditions editions){ /* Override if need */ }

	private void nameChanged() {
		if(!enabledUpdateMode) return;
		
		setButtonsEnabled(true);
		String newName = name.getText();
		fireEventInEDT(new NameChanged(node, newName));
	}


	private void decriptionChanged() {
		if(!enabledUpdateMode) return;
		
		setButtonsEnabled(true);
		String description = this.description.getText();
		fireEventInEDT(new DescriptionChanged(node, description));
	}
	
	protected void setButtonsEnabled(boolean value){
		save.setEnabled(value);
		revert.setEnabled(value);
		checkSaveButton();
	}
	
	private void checkSaveButton() {
		String newName = name.getText();
		if(Check.isEmpty(newName)){
			save.setEnabled(false);
		}
	}

	private void revert() {
		if(!enabledUpdateMode) return;
		
		NodeEditions editions = nodeEditionsAggregator.getEditions(node);
		editions.remove(UpdateName.class);
		fireEventInEDT(new NameReverted(node));
		editions.remove(UpdateDescription.class);
		fireEventInEDT(new DescriptionReverted(node));
		
		name.requestFocus();
		
		extendedRevert(editions);
		
		fillData();
	}
	
	protected void extendedRevert(NodeEditions editions){ /* Override if need */ }
	
	
	
	private void save() {
		if(!enabledUpdateMode) return;
		
		String newName = name.getText();
		String newDesctiption = description.getText();
		
		ArrayList<UpdateRequest> updates = new ArrayList<UpdateRequest>();
		updates.add(new UpdateName(newName));
		updates.add(new UpdateDescription(newDesctiption));
		updates.addAll(getExtendedUpdates());
		
		invokeSafe(new UpdateNode(node, updates));
		name.requestFocus();
		
		fillData();
		
	}
	
	protected Collection<UpdateRequest> getExtendedUpdates(){ 
		/* Override if need */ 
		return Collections.emptyList();
	}
	
	
	@EventListener
	public void onNodeChanged(NodeChanged event){
		processNodeEvent(event, event.node);
	}
	
	@EventListener
	public void onNodeReverted(NodeReverted event){
		processNodeEvent(event, event.node);
	}
	
	@SuppressWarnings("unchecked")
	private void processNodeEvent(Event event, Node eventNode){
		if(!enabledUpdateMode) return;
		if(event.getSender() == this) return;
		if(!eventNode.equals(node)) return;
		
		try {
			N node = (N) eventNode;
			setNode(node);
		}catch (ClassCastException e) {
			log.error("class cast exception", e);
		}

	}



	
	
	

}
