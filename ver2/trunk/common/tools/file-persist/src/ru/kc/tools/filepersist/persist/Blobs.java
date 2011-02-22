package ru.kc.tools.filepersist.persist;

import java.io.File;
import java.io.IOException;

import ru.kc.tools.filepersist.impl.InitContextExt;
import ru.kc.tools.filepersist.model.impl.Container;
import ru.kc.tools.filepersist.model.impl.NodeBean;
import ru.kc.tools.filepersist.persist.transaction.AtomicActionListener;
import ru.kc.tools.filepersist.persist.transaction.actions.RemoveNodeFromContainer;
import ru.kc.util.file.FileUtil;

public class Blobs {
	
	private File blobsDir;
	private File nodesDir;

	public void init(FSContext c) {
		InitContextExt init = c.c.init;
		blobsDir = init.blobsDir;
		nodesDir = init.nodesDir;
		c.actionListeners.addListener(RemoveNodeFromContainer.class, new AtomicActionListener<RemoveNodeFromContainer>() {

			@Override
			public void onInvoke(RemoveNodeFromContainer action) throws Throwable {
				moveTextToTempDir(action.container, action.node.getId());
			}

			@Override
			public void onRollback(RemoveNodeFromContainer action) throws Throwable {
				restoreTextFromTempDir(action.container, action.node.getId());
			}
			
			@Override
			public void onTransactionCommitted(RemoveNodeFromContainer action) {
				deleteTextInTempDir(action.container, action.node.getId());
			}
		});
		
	}



	public String getText(NodeBean node) throws Exception {
		File path = getTextPath(node);
		if(!path.exists()) return null;
		return getText(path);
	}
	
	public void setText(NodeBean node, String text) throws Exception {
		File path = getTextPath(node);
		setText(path, text);
	}
	
	
	

	public File getTextPath(NodeBean node) {
		File file = getTextFile(node);
		createDirs(file);
		return file;
	}

	private File getTextFile(NodeBean node){
		return getTextFile(node.getContainer(), node.getId());
	}
	
	private File getTextFile(Container container, String nodeId){
		String containerSimplePath = getFileSimplePath(container);
		String path = blobsDir.getPath()+"/"+containerSimplePath+"/"+nodeId+".txt";
		path = normalizePath(path);
		File file = new File(path);
		return file;
	}
	
	private String getFileSimplePath(Container container) {
		String path = container.getFile().getParentFile().getPath();
		String rootPath = nodesDir.getPath();
		if(rootPath.equals(path)) return "";
		
		String out = path.substring(rootPath.length()+1);
		return out;
	}

	private String normalizePath(String path) {
		path = path.replace("\\", "/");
		path = path.replace("//", "/");
		return path;
	}
	
	private void createDirs(File file) {
		file.getParentFile().mkdirs();
	}
	
	private String getText(File path) throws IOException {
		return FileUtil.readFileUTF8(path);
	}

	private void setText(File path, String text) throws IOException {
		FileUtil.writeFileUTF8(path, text);
	}

	
	
	protected void moveTextToTempDir(Container container, String nodeId) {
		File file = getTextFile(container,nodeId);
		if(file.exists()){
			File tempFile = getTempFile(container, nodeId);
			tempFile.getParentFile().mkdir();
			boolean success = file.renameTo(tempFile);
			if(! success)
				throw new IllegalStateException("can't move "+file+" to "+tempFile);
		}
	}


	protected void restoreTextFromTempDir(Container container, String nodeId) {
		File tempFile = getTempFile(container, nodeId);
		if(tempFile.exists()){
			File file = getTextFile(container, nodeId);
			boolean success = tempFile.renameTo(file);
			if(! success)
				throw new IllegalStateException("can't move "+tempFile+" to "+file);
		}
	}
	

	protected void deleteTextInTempDir(Container container, String nodeId) {
		File tempFile = getTempFile(container, nodeId);
		if(tempFile.exists()){
			tempFile.delete();
		}
	}
	
	private File getTempFile(Container container, String nodeId) {
		File path = getTextFile(container,nodeId);
		String name = path.getName();
		File dir = path.getParentFile();
		File tempDir = new File(dir,".hist");
		File out = new File(tempDir,name);
		return out;
	}
	

}
