package ru.kc.components.dialog;

import java.awt.Frame;

import ru.kc.components.dialog.ui.OneTextFieldDialog;
import ru.kc.platform.module.DialogModule;

public class OneTextFieldModule extends DialogModule<OneTextFieldDialog>{
	
	OneTextFieldDialog dialog;

	@Override
	protected OneTextFieldDialog createUI(Frame parent, boolean modal) {
		dialog = new OneTextFieldDialog(parent, modal);
		return dialog;
	}
	
	public void setFieldName(String name){
		if(!name.endsWith(":"))
			name = name+":";
		dialog.panel.fieldName.setText(name);
	}
	
	public void setTitle(String title){
		dialog.setTitle(title);
	}
	
	public String getText(){
		return getController(OneTextFieldController.class).getText();
	}

}
