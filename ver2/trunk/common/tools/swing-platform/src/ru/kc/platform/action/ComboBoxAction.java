package ru.kc.platform.action;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComboBox;

import ru.kc.platform.action.facade.ComboBoxFacade;
import ru.kc.util.swing.combobox.ListComboBoxModel;

public class ComboBoxAction extends AbstractAction  implements ComboBoxFacade {
	
	private String group;
	private JComboBox combo = new JComboBox();
	
	public ComboBoxAction() {
		combo.setMaximumSize(new Dimension(100, 30));
	}

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
	public void setToolTipText(String text){
		combo.setToolTipText(text);
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

	@Override
	public void requestFocus() {
		combo.requestFocus();
	}

	@Override
	public String getGroup() {
		return group;
	}

	@Override
	public void setGroup(String group) {
		this.group = group;
	}





}
