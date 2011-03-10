package ru.kc.module.properties.filelink;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ru.kc.common.node.edit.NodeEditions;
import ru.kc.common.node.edit.event.PathChanged;
import ru.kc.common.node.edit.event.PathReverted;
import ru.kc.model.FileLink;
import ru.kc.module.properties.node.AbstractNodePropsController;
import ru.kc.module.properties.ui.FileLinkProps;
import ru.kc.platform.annotations.Mapping;
import ru.kc.tools.filepersist.update.UpdatePath;
import ru.kc.tools.filepersist.update.UpdateRequest;
import ru.kc.util.Check;
import ru.kc.util.swing.os.OSUtil;

@Mapping(FileLinkProps.class)
public class FileLinkPropsController extends AbstractNodePropsController<FileLink, FileLinkProps> {
	
	@Override
	protected void init() {
		init(ui.name, ui.description, ui.save, ui.revert);
		
		ui.path.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				pathChanged();
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				pathChanged();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				pathChanged();
			}
		});
		
		ui.open.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String text = ui.path.getText();
					if(!Check.isEmpty(text)){
						//OSUtil.openUrl(text);
					}
				}catch (Exception ex) {
					log.error("can't open url", ex);
				}
			}
		});
		
		ui.fileChooser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int returnVal = fc.showOpenDialog(rootUI);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					ui.path.setText(file.getAbsolutePath());
				}
			}
		});
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Collection<JComponent> getComponentsToCtrlS_Save() {
		return (Collection)Arrays.asList(ui.path);
	}

	protected void pathChanged() {
		if(!enabledUpdateMode) return;
		
		setButtonsEnabled(true);
		String newPath = ui.path.getText();
		fireEventInEDT(new PathChanged(node, newPath));
	}
	
	@Override
	protected void extendedFillData(NodeEditions editions) {
		String path = editions.get(UpdatePath.class);
		
		ui.path.setText(path == null? node.getPath() : path);
		ui.path.setCaretPosition(0);
		if(path != null)
			setButtonsEnabled(true);
	}
	
	
	@Override
	protected void extendedRevert(NodeEditions editions) {
		editions.remove(UpdatePath.class);
		fireEventInEDT(new PathReverted(node));
	}
	
	@Override
	protected Collection<UpdateRequest> getExtendedUpdates() {
		String newPath = ui.path.getText();
		UpdateRequest out = new UpdatePath(newPath);
		return Arrays.asList(out);
	}
	
	


}
