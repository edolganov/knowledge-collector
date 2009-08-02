package ru.chapaj.tool.link.collector.command;

public abstract class Command {
	
	public abstract <T> T invoke();
	
}
