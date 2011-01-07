package ru.kc.tools.filepersist.persist;

import java.io.File;
import java.io.IOException;

import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.util.xml.ObjectToXMLConverter;
import ru.kc.util.xml.XmlStore;

public class PersistManager {
	

	File rootDir;
	FileNameGenerator fileNameGenerator;
	XmlStore<NodeBean> xmlStore;

	public void init(File rootDir) {
		this.rootDir = rootDir;
		fileNameGenerator = new FileNameGenerator(rootDir);
		xmlStore = new XmlStore<NodeBean>() {
			
			@Override
			protected void config(ObjectToXMLConverter<NodeBean> converter) {
				
			}
		};
	}

	public NodeBean getRootNode() throws IOException {
		File firstFile = fileNameGenerator.getFirstFile();
		if(firstFile.exists()){
			NodeBean node = load(firstFile);
			return node;
		} else {
			return null;
		}
	}


	public void saveNode(NodeBean node) throws IOException {
		NodeBean parent = node.getParent();
		if(parent != null){
			
		}else {
			saveRoot(node);
		}
		
	}

	private void saveRoot(NodeBean node) throws IOException {
		File firstFile = fileNameGenerator.getFirstFile();
		save(firstFile,node);
	}
	
	private void save(File file, NodeBean node) throws IOException {
		file.mkdirs();
		xmlStore.saveFile(file, node);
	}

	private NodeBean load(File file) throws IOException {
		return xmlStore.loadFile(file);
	}

}
