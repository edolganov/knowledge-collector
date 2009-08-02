package ru.chapaj.tool.link.collector.ui.dialog;

import javax.swing.JOptionPane;

import ru.chapaj.tool.link.collector.App;

public class SimpleConfirmDialog {
	public static boolean confirm(String label, String text){
		App.getDefault().setStopGlobalKeys(true);
		//confirm
		Object[] options = {"Ok",
		                    "Cancel"};
		int n = JOptionPane.showOptionDialog(App.getDefault().getUI(),
			text,
		    label,
		    JOptionPane.OK_CANCEL_OPTION,
		    JOptionPane.WARNING_MESSAGE,
		    null,
		    options,
		    options[1]);
		try {
			//System.out.println(Thread.currentThread().getName());
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		App.getDefault().setStopGlobalKeys(false);
		return n == 0;
	}
}
