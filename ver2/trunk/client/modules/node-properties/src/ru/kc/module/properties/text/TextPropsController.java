package ru.kc.module.properties.text;

import java.util.Arrays;
import java.util.Collection;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ru.kc.common.node.edit.NodeEditions;
import ru.kc.common.node.edit.event.TextChanged;
import ru.kc.common.node.edit.event.TextReverted;
import ru.kc.model.Text;
import ru.kc.module.properties.node.AbstractNodePropsController;
import ru.kc.module.properties.ui.TextProps;
import ru.kc.platform.annotations.Mapping;
import ru.kc.tools.filepersist.update.UpdateRequest;
import ru.kc.tools.filepersist.update.UpdateText;

@Mapping(TextProps.class)
public class TextPropsController extends AbstractNodePropsController<Text, TextProps> {
	
	@Override
	protected void init() {
		init(ui.name, ui.description, ui.save, ui.revert);
		
		ui.text.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				textChanged();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				textChanged();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				textChanged();
			}
		});
		
	}
	
	protected void textChanged() {
		if(!enabledUpdateMode) return;
		
		setButtonsEnabled(true);
		String newText = ui.text.getText();
		fireEventInEDT(new TextChanged(node, newText));
	}
	
	@Override
	protected void extendedFillData(NodeEditions editions) {
		String text = editions.get(UpdateText.class);
		
		ui.text.setText(text == null? node.safeGetText() : text);
		ui.text.setCaretPosition(0);
		if(text != null)
			setButtonsEnabled(true);
	}
	

	@Override
	protected void extendedRevert(NodeEditions editions) {
		editions.remove(UpdateText.class);
		fireEventInEDT(new TextReverted(node));
	}
	
	@Override
	protected Collection<UpdateRequest> getExtendedUpdates() {
		String newText = ui.text.getText();
		UpdateRequest out = new UpdateText(newText);
		return Arrays.asList(out);
	}
	
	


}
