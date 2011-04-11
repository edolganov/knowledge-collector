package ru.kc.module.snapshots.tools;

import ru.kc.model.Node;
import ru.kc.module.snapshots.model.Snapshot;
import ru.kc.tools.filepersist.update.SetProperty;

import com.google.gson.Gson;

public class SnapshotConverter {
	
	public static final String CUR_SNAPSHOT_KEY = "cur-snapshot";
	
	public Snapshot loadFrom(Node owner){
		String data = owner.getProperty(CUR_SNAPSHOT_KEY);
		if(data == null){
			return null;
		}
		
		Snapshot out = new Gson().fromJson(data, Snapshot.class);
		return out;
	}
	
	
	public SetProperty createUpdate(Snapshot snapshot){
		String data = new Gson().toJson(snapshot);
		return new SetProperty(CUR_SNAPSHOT_KEY, data);
	}

}
