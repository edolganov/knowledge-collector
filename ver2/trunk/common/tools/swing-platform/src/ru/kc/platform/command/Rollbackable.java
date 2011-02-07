package ru.kc.platform.command;

public interface Rollbackable {
	
	void rollback() throws Exception;

}
