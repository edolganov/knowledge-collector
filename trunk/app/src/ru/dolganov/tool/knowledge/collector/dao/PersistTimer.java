package ru.dolganov.tool.knowledge.collector.dao;

import java.util.HashMap;
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



		public TimerInfo(String key, PersistTimer owner) {
			super();
			this.key = key;
			this.owner = owner;
		}

		@Override
		public void run() {
			owner.timeOut(key);
		}
	}
	
	public static interface TimeoutListener {
		void onTimeout(String key);
	}
	
	private Object lock = new Object();
	private HashMap<String, TimerInfo> requests = new HashMap<String, TimerInfo>();
	private TimeoutListener timeoutListener;
	private long delay;
	
	
	public PersistTimer(TimeoutListener listener,long delay) {
		this.timeoutListener = listener;
		this.delay = delay;
	}
	
	public void start(String key){
		synchronized (lock) {
			TimerInfo info = requests.remove(key);
			if(info != null) info.cancel();
			
			info = new TimerInfo(key,this);
			requests.put(key, info);
			timer.schedule(info, delay);
		}
	}
	
	private void timeOut(String key){
		synchronized (lock) {
			timeoutListener.onTimeout(key);
		}
	}
	

}
