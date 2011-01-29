package ru.kc.util.file.redeploy;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RedeployManager {
	protected static Log log = LogFactory.getLog(RedeployManager.class);
	
	
	private Timer timer;
	private RedeployManagerListener l;

	
	//data
	private Lock lock = new ReentrantLock();
	private Map<String,FileInfo> files = new HashMap<String, FileInfo>();
	private boolean firstCheck = true;
	private CopyOnWriteArrayList<String> filesBase = new CopyOnWriteArrayList<String>();
	TimerTask task;
	

	public RedeployManager(final String fileSuffix, RedeployManagerListener listener) {
		this.l = listener;
		
		timer = new Timer("RedeployManager-file-scan-thread",true);
		task = new TimerTask() {
			
			@Override
			public void run() {
				for (String baseDir : filesBase) {
					try {
						String dirPath = baseDir;
						if(dirPath == null) return;
						
						LinkedList<File> queue = new LinkedList<File>();
						queue.addLast(new File(dirPath));
						
						LinkedList<File> scriptsList = new LinkedList<File>();
						while(!queue.isEmpty()){
							File dir = queue.removeFirst();
							File[] scripts = dir.listFiles();
							for (File f : scripts) {
								if(!f.isDirectory() && f.getName().endsWith(fileSuffix)){
									scriptsList.addLast(f);
								} else if(f.isDirectory()){
									queue.addLast(f);
								}
								
							}
						}
						
						if(firstCheck){
							addFiles(scriptsList);
							firstCheck = false;
						} else {
							checkFiles(scriptsList);
						}
					}catch (Exception e) {
						log.error(e.getMessage());
					}
				}

			}
			
		};

	}
	

	public void start(){
		timer.schedule(task, 1000, 3000);
	}
	
	public void stop(){
		timer.cancel();
	}
	
	public void addFilesDir(String path){
		filesBase.add(path);
	}

	




	private void checkFiles(List<File> list) {
		//собираем инфу по файлам
		HashSet<String> paths = new HashSet<String>();
		HashMap<String,FileInfo> infos = new HashMap<String,FileInfo>();
		for (File f : list) {
			String path = f.getAbsolutePath();
			paths.add(path);
			infos.put(path,new FileInfo(f.lastModified()));
		}
		
		Collection<String> deleted = checkDeleted(paths);
		for (String path : deleted) {
			infos.remove(path);
		}
		for(String path : infos.keySet()){
			checkFile(path, infos.get(path));
		}
	}


	private void checkFile(String path, FileInfo fileInfo) {
		lock.lock();
		try{
			FileInfo info = files.get(path);
			if(info != null){
				if(info.lastModified != fileInfo.lastModified){
					FileInfo old = files.put(path, fileInfo);
					if(!old.skipChange){
						try{
							//log.info("updated: "+path);
							l.onUpdate(path);
						}catch (Exception e) {
							log.error(e.getMessage());
						}
						
					}
				}
			} else {
				//new file
				files.put(path, fileInfo);
				try{
					//log.info("created: "+path);
					l.onCreate(path);
				}catch (Exception e) {
					log.error(e.getMessage());
				}
				
				return;
			}
			
		}finally{
			lock.unlock();
		}
	}



	/**
	 * находим удаленные файлы
	 * @param paths
	 */
	private Collection<String> checkDeleted(Collection<String> paths) {
		ArrayList<String> deleted = new ArrayList<String>();
		
		lock.lock();
		try{
			Set<String> keySet = files.keySet();
			for (String path : keySet) {
				if(!paths.contains(path)){
					deleted.add(path);
				}
			}
			
			for (String path : deleted) {
				FileInfo info = files.remove(path);
				if(!info.skipChange){
					try{
						log.info("deleted: "+path);
						l.onDelete(path);
					}catch (Exception e) {
						log.error(e.getMessage());
					}

				}
			}
		}finally{
			lock.unlock();
		}
		
		return deleted;
	}
	
	protected void addFiles(List<File> scriptsList) {
		lock.lock();
		try{
			for (File f : scriptsList) {
				files.put(f.getAbsolutePath(), new FileInfo(f.lastModified()));
			}
		}finally{
			lock.unlock();
		}	
	}
	
	public void skipChange(File file) {
		lock.lock();
		try{
			String path = file.getAbsolutePath();
			FileInfo fileInfo = files.get(path);
			if(fileInfo == null){
				fileInfo = new FileInfo(0);
				files.put(path, fileInfo);
			}
			fileInfo.skipChange = true;
			
		}finally{
			lock.unlock();
		}	
	}








	
	

}
