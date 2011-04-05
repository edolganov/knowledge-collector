package ru.kc.module.snapshots.tools;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ru.kc.model.Node;
import ru.kc.module.snapshots.model.SnapshotDir;
import ru.kc.module.snapshots.model.update.AbstractSnapshotsUpdate;
import ru.kc.tools.filepersist.update.SetProperty;

public class SnapshotConverter {
	
	private static final String SNAPSHOTS_PROPERTY_KEY = "snapshots";
	
	public List<SnapshotDir> loadFrom(Node node){
		String data = node.getProperty(SNAPSHOTS_PROPERTY_KEY);
		if(data == null)
			return new ArrayList<SnapshotDir>();
		
		Type listType = new TypeToken<List<SnapshotDir>>(){}.getType();
		List<SnapshotDir> list = new Gson().fromJson(data, listType);
		return list;
	}
	
	
	public SetProperty createUpdate(List<SnapshotDir> snapshotDirs, AbstractSnapshotsUpdate additionInfo){
		String data = new Gson().toJson(snapshotDirs);
		return new SetProperty(SNAPSHOTS_PROPERTY_KEY, data, additionInfo);
	}

}
