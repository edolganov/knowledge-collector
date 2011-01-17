package ru.kc.tools.filepersist.persist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.util.file.FileUtil;
import ru.kc.util.xml.ObjectToXMLConverter;
import ru.kc.util.xml.XmlStore;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class ContainerStore {
	
	//private static Log log = LogFactory.getLog(ContainerStore.class);
	
	private XmlStore<Container> xmlStore = new XmlStore<Container>() {
		
		@Override
		protected void config(ObjectToXMLConverter<Container> converter) {
			String packName = "ru.kc.tools.filepersist";
			Reflections reflections = new Reflections(new ConfigurationBuilder()
	        .setUrls(ClasspathHelper.getUrlsForPackagePrefix(packName))
	        .setScanners(new TypeAnnotationsScanner())
	        .filterInputsBy(new FilterBuilder.Include(FilterBuilder.prefix(packName))));
			
			Set<Class<?>> all = reflections.getTypesAnnotatedWith(XStreamAlias.class);
			if(all == null || all.size() == 0) throw new IllegalStateException("no data model classes with annotation @XStreamAlias");
			//log.info("found "+all.size()+" classes");
			
			for (Class<?> clazz : all) {
				converter.configureAliases(clazz);
			}
		}
	};
	
	private FSContext c;
	
	public void init(FSContext c){
		this.c = c;
	}
	
	
	public void save(Container container) throws IOException {
		container.getFile().getParentFile().mkdirs();
		createHistoryFile(container);
		container.setRevision(container.getRevision()+1);
		xmlStore.saveFile(container.getFile(), container);
	}



	public Container load(File file) throws IOException{
		Container container = xmlStore.loadFile(file);
		container.init(file, c.c);
		
		ArrayList<NodeBean> nodes = container.getNodes();
		for (NodeBean nodeBean : nodes) {
			nodeBean.setContainer(container);
		}
		return container;
	}

	public void rollback(Container container)throws IOException {
		File resourse = container.getFile();
		long oldRevision = container.getRevision() - 1;
		
		File historyFile = getHistoryFile(resourse, oldRevision);
		if(!historyFile.exists()){
			throw new IllegalStateException("file "+historyFile+" not exist, can't rollback "+container);
		}
		
		File temp = new File(resourse.getParent(),resourse.getName()+".tmp");
		if(temp.exists()){
			throw new IllegalStateException("temp file "+temp+" already exist, can't rollback "+container);
		}
		
		boolean success = resourse.renameTo(temp);
		if(!success){
			throw new IllegalStateException("can't rename "+resourse+" to "+temp+", can't rollback "+container);
		}
		
		success = historyFile.renameTo(resourse);
		if(!success){
			throw new IllegalStateException("can't rename "+historyFile+" to "+resourse+", can't rollback "+container);
		}
		temp.delete();
		
		container.setRevision(oldRevision);
	}
	
	
	private void createHistoryFile(Container container) throws IOException{
		File resourse = container.getFile();
		if(!resourse.exists()) return;
		
		long revision = container.getRevision();
		
		//create new history file
		File destFile = getHistoryFile(resourse,revision);
		if(destFile.exists()) throw new IllegalStateException("Found already persisted revision "+revision+" of "+container);
		try {
			FileUtil.copyFile(resourse, destFile);
		} catch (IOException e) {
			destFile.delete();
			throw e;
		}
		
		//delete old history file
		File toDelete = getHistoryFile(resourse,revision-1);
		toDelete.delete();
	}
	
	private File getHistoryFile(File resourse, long revision) {
		String backupFileName = resourse.getName()+"."+revision;
		
		File backupDir = new File(resourse.getParentFile(),".hist");
		backupDir.mkdir();
		
		File out = new File(backupDir,backupFileName);
		return out;
	}


	public Container load(String simpleFilePath) throws IOException {
		File containerFile = null;
		File nodesDir = c.c.init.nodesDir;
		String nodesDirPath = nodesDir.getPath();
		if(simpleFilePath.startsWith(nodesDirPath)){
			containerFile = new File(simpleFilePath);
		} else {
			containerFile = new File(nodesDirPath+"/"+simpleFilePath);
		}
		return load(containerFile);
	}

}
