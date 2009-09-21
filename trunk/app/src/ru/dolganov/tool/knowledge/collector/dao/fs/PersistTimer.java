package ru.dolganov.tool.knowledge.collector.dao.fs;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Таймер для сохранения файла
 * @author jenua.dolganov
 *
 */
public class PersistTimer {
	
	private Timer timer = new Timer();
	
	private static class TimerInfo extends TimerTask {
		
		String key;
		PersistTimer owner;
		Map<SaveOps, Object[]> saveOps;
		LinkedList<Map<SaveOps, Object[]>> oldSaveOps;



		public TimerInfo(String key, PersistTimer owner,Map<SaveOps, Object[]> saveOps,LinkedList<Map<SaveOps, Object[]>> oldSaveOps) {
			super();
			this.key = key;
			this.owner = owner;
			this.saveOps = saveOps;
			this.oldSaveOps = oldSaveOps;
		}

		@Override
		public void run() {
			Map<SaveOps, Object[]> saveOps = this.saveOps;
			LinkedList<Map<SaveOps, Object[]>> oldSaveOps = this.oldSaveOps;
			this.saveOps = null;
			this.oldSaveOps = null;
			owner.timeOut(key,saveOps,oldSaveOps);
		}
	}
	
	public static interface TimeoutListener {
		void onTimeout(String key,Map<SaveOps, Object[]> saveOps, List<Map<SaveOps,Object[]>> oldSaveOps);
	}
	
	private Object lock = new Object();
	private HashMap<String, TimerInfo> requests = new HashMap<String, TimerInfo>();
	private TimeoutListener timeoutListener;
	private long delay;
	
	
	public PersistTimer(TimeoutListener listener,long delay) {
		this.timeoutListener = listener;
		this.delay = delay;
	}
	
	public void start(String key, Map<SaveOps, Object[]> saveOps){
		synchronized (lock) {
			TimerInfo info = requests.remove(key);
			LinkedList<Map<SaveOps, Object[]>> oldSaveOps = null;
			if(info != null) {
				info.cancel();
				oldSaveOps = info.oldSaveOps;
				if(oldSaveOps != null) oldSaveOps.add(info.saveOps);
			}
			if(oldSaveOps == null) oldSaveOps = new LinkedList<Map<SaveOps,Object[]>>();
			
			info = new TimerInfo(key,this,saveOps,oldSaveOps);
			requests.put(key, info);
			timer.schedule(info, delay);
		}
	}
	
	private void timeOut(String key,Map<SaveOps, Object[]> saveOps, List<Map<SaveOps,Object[]>> oldSaveOps){
		synchronized (lock) {
			timeoutListener.onTimeout(key,saveOps,oldSaveOps);
			requests.remove(key);
		}
	}
	

}
