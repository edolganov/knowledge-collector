package ru.kc.module.imports.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import ru.kc.common.controller.Controller;
import ru.kc.model.Node;
import ru.kc.module.imports.ui.ImportOldDataDialog;
import ru.kc.platform.annotations.Mapping;

@Mapping(ImportOldDataDialog.class)
public class ImportOldDataController extends Controller<ImportOldDataDialog> {
	
	JTextArea textArea;
	Node importRoot;
	File dataDir;
	boolean abort;

	@Override
	protected void init() {
		textArea = ui.importPanel1.text;
		textArea.setEditable(false);
		
		ui.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				startImport();
			}
			
		});
		
		ui.okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ui.dispose();
			}
		});
		ui.okButton.setEnabled(false);
		
		ui.cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public void setImportData(Node importRoot, File dataDir){
		this.importRoot = importRoot;
		this.dataDir = dataDir;
	}
	
	@SuppressWarnings("rawtypes")
	protected void startImport() {
		final ImportOldDataTextModel textModel = new ImportOldDataTextModel();
		textModel.title = "Import old data log";
		textModel.searchTitle = "Searching old data...";
		
		SwingWorker worker = new SwingWorker() {

			@Override
			protected Object doInBackground() throws Exception {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			protected void process(List chunks) {
				textArea.setText(textModel.getText());
			}
			
			@Override
			protected void done() {
				textArea.setText(textModel.getText());
				ui.okButton.setEnabled(true);
				ui.cancelButton.setEnabled(false);
			}
		}; 
		worker.execute();
	}

}
