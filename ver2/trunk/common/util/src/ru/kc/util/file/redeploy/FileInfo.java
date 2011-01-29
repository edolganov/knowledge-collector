package ru.kc.util.file.redeploy;

public class FileInfo {
	
	public final long lastModified;
	public boolean skipChange;

	public FileInfo(long lastModified) {
		super();
		this.lastModified = lastModified;
	}
	
	
}