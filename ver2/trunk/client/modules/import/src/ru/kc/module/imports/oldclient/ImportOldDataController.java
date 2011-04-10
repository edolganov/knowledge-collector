package ru.kc.module.imports.oldclient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import ru.kc.common.controller.Controller;
import ru.kc.model.Dir;
import ru.kc.model.FileLink;
import ru.kc.model.Link;
import ru.kc.model.Node;
import ru.kc.model.Text;
import ru.kc.module.imports.oldclient.model.ImportOldDataTextModel;
import ru.kc.module.imports.oldclient.oldmodel.FileNameConverter;
import ru.kc.module.imports.oldclient.oldmodel.OldDir;
import ru.kc.module.imports.oldclient.oldmodel.OldLocalLink;
import ru.kc.module.imports.oldclient.oldmodel.OldNetworkLink;
import ru.kc.module.imports.oldclient.oldmodel.OldNode;
import ru.kc.module.imports.oldclient.oldmodel.OldNote;
import ru.kc.module.imports.oldclient.oldmodel.Root;
import ru.kc.module.imports.oldclient.oldmodel.RootElement;
import ru.kc.module.imports.ui.ImportOldDataDialog;
import ru.kc.platform.annotations.Mapping;
import ru.kc.tools.filepersist.update.UpdateText;
import ru.kc.util.file.FileUtil;

@Mapping(ImportOldDataDialog.class)
public class ImportOldDataController extends Controller<ImportOldDataDialog> {
	
	private static class Info {
		public final File dataDir;
		public final Node parentNode;
		public Info(File dataDir, Node parentNode) {
			super();
			this.dataDir = dataDir;
			this.parentNode = parentNode;
		}
	}
	
	private JTextArea textArea;
	private Node importRoot;
	private File dataDir;
	//private boolean abort; //TODO
	private ImportOldDataTextModel textModel = new ImportOldDataTextModel();
	private DataLoader dataLoader = new DataLoader();
	private volatile Exception stopImportException = null;
	private HashMap<String, String> textToSave = new HashMap<String, String>();
	private HashMap<String, Node> createdNodes = new HashMap<String, Node>();

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
	
	protected void startImport() {
		
		SwingWorker<Object, List<ConvertInfo>> worker = new SwingWorker<Object, List<ConvertInfo>>() {

			@Override
			protected Object doInBackground() throws Exception {
				
				textModel.addText("Scanning "+dataDir.getAbsolutePath());
				File rootData = findDataFile();
				if(rootData == null){
					textModel.errorEnd("Can't find data dir");
					return null;
				}
				
				Root root = dataLoader.loadFile(rootData);
				List<RootElement> nodes = root.getNodes();
				if(nodes.size() < 1){
					textModel.addText("No data to import");
					textModel.successEnd();
					return null;
				}
				
				File firstNodeDir = getExistsNodeDir(rootData, nodes.get(0));
				if(firstNodeDir == null){
					textModel.addText("No data to import");
					textModel.successEnd();
					return null;
				}
				
				LinkedList<Info> queue = new LinkedList<Info>();
				queue.addLast(new Info(firstNodeDir, importRoot));
				
				try {
					while( ! queue.isEmpty()){
						checkForStopImportException();
						
						Info info = queue.removeFirst();
						File dataFile = getDataFile(info.dataDir);
						if(dataFile == null) continue;
						
						Root curRoot = dataLoader.loadFile(dataFile);
						textModel.addText("\tProcessing "+dataFile.getAbsolutePath());
						
						final ArrayList<ConvertInfo> converted = new ArrayList<ConvertInfo>();
						for(RootElement elem : curRoot.getNodes()){
							if(elem instanceof OldDir){
								converted.add(convert(info.parentNode, (OldDir)elem, dataFile));
							}
							else if(elem instanceof OldNote){
								converted.add(convert(info.parentNode, (OldNote)elem, dataFile));
							}
							else if(elem instanceof OldNetworkLink){
								converted.add(convert(info.parentNode, (OldNetworkLink)elem, dataFile));
							}
							else if(elem instanceof OldLocalLink){
								converted.add(convert(info.parentNode, (OldLocalLink)elem, dataFile));
								
							} else {
								log.info("\t\tCan't add unknow object "+elem);
							}
						}
						
						SwingUtilities.invokeAndWait(new Runnable() {
							
							@Override
							public void run() {
								textArea.setText(textModel.getText());
								addToTree(converted);
							}
						});
						checkForStopImportException();
						
						for(ConvertInfo convertedInfo : converted){
							RootElement oldNode = convertedInfo.oldNode;
							File nodeDir = getExistsNodeDir(dataFile, oldNode);
							if(nodeDir == null) continue;
							Node parentNode = createdNodes.remove(convertedInfo.node.getId());
							queue.addLast(new Info(nodeDir, parentNode));
						}
					}
					textModel.successEnd();
					
				}catch (Exception e) {
					textModel.errorEnd(e.getMessage());
				}
				
				return null;
			}


			private void checkForStopImportException() throws Exception {
				if(stopImportException != null){
					throw stopImportException;
				}
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
	
	private void addToTree(List<ConvertInfo> list){
		if(stopImportException != null) return;
		
		try {
			for(ConvertInfo info : list){
				Node child = info.node;
				if(child instanceof Text){
					if(info.textForTextNode != null){
						textToSave.put(child.getId(), info.textForTextNode);
					}
				}
				persistTree.add(info.parent, child);
			}
		}catch (Exception e) {
			log.error("", e);
			stopImportException = e;
			return;
		}
	}
	
	@Override
	protected void onChildAdded(Node parent, final Node child) {
		createdNodes.put(child.getId(), child);
		
		if(child instanceof Text){
			final String toSave = textToSave.remove(child.getId());
			if(toSave == null) return;
			
			SwingUtilities.invokeLater(new Runnable() {
				
				@Override
				public void run() {
					try {
						updater.update(child, new UpdateText(toSave));
					}catch (Exception e) {
						log.error("", e);
					}
				}
			});
		}
	}
	
	
	private ConvertInfo convert(Node parentNode, OldDir elem, File dataFile) {
		String name = elem.getName();
		String description = elem.getDescription();
		Dir out = persistFactory.createDir(name, description);
		return new ConvertInfo(parentNode, elem, out);
		
	}
	
	private ConvertInfo convert(Node parentNode, OldNote elem, File dataFile) throws IOException {
		String name = elem.getName();
		String description = elem.getDescription();
		String text = getText(elem.getName(), dataFile);
		Text out = persistFactory.createText(name, description);
		return new ConvertInfo(parentNode, elem, out, text);
		
	}
	
	private String getText(String name, File dataFile) throws IOException {
		String textFileName = FileNameConverter.convertToValidFSName(name)+".txt";
		String parent = dataFile.getParent();
		File textFile = new File(parent, textFileName);
		if(textFile.exists()){
			return FileUtil.readFileUTF8(textFile);
		} else {
			return null;
		}

	}

	private ConvertInfo convert(Node parentNode, OldNetworkLink elem, File dataFile) {
		String name = elem.getName();
		String description = elem.getDescription();
		String url = elem.getUrl();
		Link out = persistFactory.createLink(name, url, description);
		return new ConvertInfo(parentNode, elem, out);
	}
	
	private ConvertInfo convert(Node parentNode, OldLocalLink elem, File dataFile) {
		String name = elem.getName();
		String description = elem.getDescription();
		String path = elem.getUrl();
		FileLink out = persistFactory.createFileLink(name, path, description);
		return new ConvertInfo(parentNode, elem, out);
	}

	

	private File findDataFile() {
		File out = getDataFile(dataDir);
		if(out == null){
			File knowDir = findFile("know", dataDir, false);
			if(knowDir != null){
				out = getDataFile(knowDir);
			}
		}
		return out;
	}
	
	protected File getDataFile(File parent){
		File[] listFiles = parent.listFiles();
		if(listFiles != null){
			for(File file : listFiles){
				if(file.isFile()){
					if("data.xml".equals(file.getName())){
						return file;
					}
				}
			}
		}
		return findFile("data.xml", parent, true);
	}
	
	protected File findFile(String name, File parent, boolean isFile){
		File[] listFiles = parent.listFiles();
		if(listFiles != null){
			for(File file : listFiles){
				if(file.isFile()){
					if(!isFile) continue;
				} else {
					if(isFile) continue;
				}
				if(name.equals(file.getName())){
					return file;
				}
			}
		}
		return null;
	}
	
	private File getExistsNodeDir(File nodeFile, RootElement elem){
		if(elem instanceof OldNode){
			String fileName = FileNameConverter.convertToValidFSName(((OldNode) elem).getName());
			File childDir = new File(nodeFile.getParentFile(), fileName);
			if(childDir.exists() && childDir.isDirectory()){
				return childDir;
			} else {
				return null;
			}
		} else {
			return null;
		}
		
	}

}
