package ru.kc.tools.filepersist.persist.transaction.model;

import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("journal-record")
public class JournalRecord {
	
	private long createDate;
	private boolean finished;
	private String sessionId;
	private int numberInSession;
	private ArrayList<Object> events = new ArrayList<Object>();


	public void addEvent(Object event) {
		events.add(event);
	}
	
	
	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}


	public ArrayList<Object> getEvents() {
		return events;
	}


	public void setEvents(ArrayList<Object> events) {
		this.events = events;
	}


	public String getSessionId() {
		return sessionId;
	}


	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}


	public int getNumberInSession() {
		return numberInSession;
	}


	public void setNumberInSession(int numberInSession) {
		this.numberInSession = numberInSession;
	}


	public boolean isFinished() {
		return finished;
	}


	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
	
	
	
	

}
