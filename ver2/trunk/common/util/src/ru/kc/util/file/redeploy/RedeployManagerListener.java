package ru.kc.util.file.redeploy;

public interface RedeployManagerListener {
	
	void onCreate(String path);
	
	void onUpdate(String path);
	
	void onDelete(String path);

}
