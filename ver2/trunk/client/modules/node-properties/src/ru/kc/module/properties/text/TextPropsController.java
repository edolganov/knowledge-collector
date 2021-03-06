package ru.kc.module.properties.text;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ru.kc.common.node.edit.NodeEditions;
import ru.kc.common.node.edit.event.TextChanged;
import ru.kc.common.node.edit.event.TextReverted;
import ru.kc.common.node.event.OpenNodeRequest;
import ru.kc.model.Text;
import ru.kc.module.properties.node.AbstractNodePropsController;
import ru.kc.module.properties.ui.TextProps;
import ru.kc.platform.annotations.Mapping;
import ru.kc.tools.filepersist.update.UpdateRequest;
import ru.kc.tools.filepersist.update.UpdateText;
import ru.kc.util.swing.text.TextComponentUtil;

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
		
		ui.open.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				openNodeRequest();
			}
		});
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Collection<JComponent> getComponentsToCtrlS_Save() {
		return (Collection)Arrays.asList(ui.text);
	}

	protected void textChanged() {
		if(!enabledUpdateMode) return;
		
		setButtonsEnabled(true);
		String newText = ui.text.getText();
		fireEvent(new TextChanged(node, newText));
	}
	
	protected void openNodeRequest() {
		if(node == null) return;
		fireEvent(new OpenNodeRequest(node));
	}
	
	@Override
	protected void extendedFillData(NodeEditions editions) {
		String text = editions.get(UpdateText.class);
		TextComponentUtil.setTextWithSaveCaretPosition(ui.text, text == null? node.safeGetText() : text);
		if(text != null)
			setButtonsEnabled(true);
	}
	

	@Override
	protected void extendedRevert(NodeEditions editions) {
		editions.remove(UpdateText.class);
		fireEvent(new TextReverted(node));
	}
	
	@Override
	protected Collection<UpdateRequest> getExtendedUpdates() {
		String newText = ui.text.getText();
		UpdateRequest out = new UpdateText(newText);
		return Arrays.asList(out);
	}
	
	


}
