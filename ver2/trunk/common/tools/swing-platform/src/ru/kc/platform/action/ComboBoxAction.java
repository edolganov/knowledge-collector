package ru.kc.platform.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComboBox;

import ru.kc.platform.action.facade.ComboBoxFacade;
import ru.kc.util.swing.combobox.ListComboBoxModel;

public class ComboBoxAction  implements ComboBoxFacade, AbstractAction{
	
	private JComboBox combo = new JComboBox();

	@Override
	public void enabledRequest() {
		combo.setEnabled(true);
	}

	@Override
	public void disable() {
		combo.setEnabled(false);
	}

	@Override
	public void setValues(List<Object> values) {
		combo.setModel(new ListComboBoxModel<Object>(values));
	}
	
	@Override
	public void selectValue(int index) {
		combo.setSelectedIndex(index);
	}
	
	@Override
	public void addListener(final Listener listener) {
		combo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.onSelected(combo.getSelectedItem());
			}
		});
	}
	
	@Override
	public JComboBox getComponent(){
		return combo;
	}







}
