package ru.kc.tools.filepersist;

public interface PersistService {
	
	void addListener(ServiceListener listener);
	
	Tree tree();
	
	Factory factory();
	
	Updater updater();
	
	

}
