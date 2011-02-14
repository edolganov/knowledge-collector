package ru.kc.util.swing.config;

public abstract class ObjectHandler {
	
	private ProcessPhase phase = ProcessPhase.FROM_PARENT_TO_CHILD;

	public ObjectHandler(ProcessPhase phase) {
		super();
		this.phase = phase;
	}

	public ObjectHandler() {
		super();
	}

	public ProcessPhase getPhase() {
		return phase;
	}
	
	public abstract void process(Object ob);
	
	
	
	
}
